/*
 * ******************************************************************************
 *  * Copyright (c) 2020 Sven Erik Knop.
 *  * Licensed under the EUPL V.1.2
 *  *
 *  * This Software is provided to You under the terms of the European
 *  * Union Public License (the "EUPL") version 1.2 as published by the
 *  * European Union. Any use of this Software, other than as authorized
 *  * under this License is strictly prohibited (to the extent such use
 *  * is covered by a right of the copyright holder of this Software).
 *  *
 *  * This Software is provided under the License on an "AS IS" basis and
 *  * without warranties of any kind concerning the Software, including
 *  * without limitation merchantability, fitness for a particular purpose,
 *  * absence of defects or errors, accuracy, and non-infringement of
 *  * intellectual property rights other than copyright. This disclaimer
 *  * of warranty is an essential part of the License and a condition for
 *  * the grant of any rights to this Software.
 *  *
 *  * For more details, see http://joinup.ec.europa.eu/software/page/eupl.
 *  *
 *  * Contributors:
 *  *     2020 - Sven Erik Knop - initial API and implementation
 *  ******************************************************************************
 */

package latinsquare.sudoku

import com.typesafe.scalalogging.Logger
import latinsquare.{Cell, Point, Puzzle}
import latinsquare.unit.Nonet
import latinsquare.exceptions.IllegalFileFormatException

import org.rogach.scallop._

import scala.collection.mutable
import scala.util.Random

class Sudoku extends Puzzle(9) {
    private val logger = Logger[Sudoku]
    private val rows = new mutable.ArrayBuffer[Nonet]()
    private val columns = new mutable.ArrayBuffer[Nonet]()
    private val boxes = new mutable.ArrayBuffer[Nonet]()

    // Initialisation

    for (x <- 1 to 9) {
        for (y <- 1 to 9) {
            val point = new Point(x, y)
            val cell = new Cell(9, point)
            cells.put(point, cell)
        }
    }

    for (x <- 1 to 9) {
        val row = new Nonet(s"Row $x")
        rows.addOne(row)

        for (y <- 1 to 9) {
            val point = new Point(x, y)
            val cellOpt = cells.get(point)
            cellOpt match {
                case Some(cell) => row.addCell(cell)
                case None => logger.error(s"Cannot find Cell for $point, should never happen")
            }
        }
    }

    for (y <- 1 to 9) {
        val column = new Nonet(s"Column $y")
        columns.addOne(column)

        for (x <- 1 to 9) {
            val point = new Point(x, y)
            val cellOpt = cells.get(point)
            cellOpt match {
                case Some(cell) => column.addCell(cell)
                case None => logger.error(s"Cannot find Cell for $point, should never happen")
            }
        }
    }

    for (x1 <- 0 until 3) {
        for (y1 <- 0 until 3) {
            val box = new Nonet(s"Box ${x1+1}/${y1+1}")
            boxes.addOne(box)

            for (x2 <- 1 to 3) {
                val x = x1 * 3 + x2
                for (y2 <- 1 to 3) {
                    val y = y1 * 3 + y2

                    val point = new Point(x, y)
                    val cellOpt = cells.get(point)
                    cellOpt match {
                        case Some(cell) => box.addCell(cell)
                        case None => logger.error(s"Cannot find Cell for $point, should never happen")
                    }
                }
            }
        }
    }

    override def toString: String = {
        val builder = new mutable.StringBuilder

        addNonet("Rows", rows, builder)
        addNonet("Columns", columns, builder)
        addNonet("Boxes", boxes, builder)

        builder.toString()
    }

    override def dimension: Int = 9

    override def importStrings(lines: Array[String]): Unit = {
        val values = Array.ofDim[Int](dimension, dimension)

        var row : Int = 0

        for (line <- lines) {
            val lineValues = line.split(",")
            if (lineValues.length != dimension) {
                throw new IllegalFileFormatException(s"Illegal entry in file : $line")
            }

            for (col <- 0 until 9) {
                values(row)(col) = lineValues(col).toInt
            }

            row += 1
        }

        reset()

        importArray(values)
    }

    def importArray(values : Array[Array[Int]]) : Unit = {
        for (row <- 0 until 9) {
            for (col <- 0 until 9) {
                val point : Point = new Point(row + 1, col + 1)
                val value : Int = values(row)(col)

                if (value > 0) {
                    val cellOpt = cells.get(point)

                    cellOpt match {
                        case Some(cell) => cell.value = value
                        case None => logger.error("Should never happen @" + sourcecode.File + ":" + sourcecode.Line)
                    }
                }
            }
        }
    }

    private def addNonet(name : String, nonets : mutable.Seq[Nonet], builder : mutable.StringBuilder) : Unit = {
        builder ++= name
        builder ++= "\n"

        for (nonet <- nonets) {
            builder ++= nonet.toString
            builder ++= "\n"
        }
    }

    override def createRandomPuzzle() : Unit = {
        reset()

        val random = new Random

        val independentBoxes = Array(
            Array(1, 5, 9),
            Array(1, 6, 8),
            Array(2, 4, 9),
            Array(2, 6, 7),
            Array(3, 4, 8),
            Array(3, 5, 7)
        )

        val boxSeeds = independentBoxes(random.nextInt(independentBoxes.length))
        for (i <- boxSeeds) {
            val box : Nonet = boxes(i -1)
            val cells = box.getCells

            val seed = Random.shuffle(List(1, 2, 3, 4, 5, 6, 7, 8, 9))
            for (c <- 0 until 9) {
                val cell = cells(c)
                cell.value = seed(c)
            }
        }

        solveBruteForce // this fills the whole of the Sudoku puzzle

        // now remove entries until the solution is not unique anymore
        val allCells = Random.shuffle(cells.values.toList)

        for (cell <- allCells) {
            val value = cell.value

            cell.reset()
            if (isUnique > 1) {
                cell.value = value
            }
        }

        allCells.foreach(_.readOnly = true)
    }
}

class Conf(arguments: Array[String]) extends ScallopConf(arguments.toIndexedSeq) {
    version("Sudoku 0.1 (C) 2020 Sven Erik Knop")
    banner("""Sudoku puzzle solver and generator""")
    val input : ScallopOption[String] = opt[String](descr = "Filename pointing to a file with a Sudoku puzzle in CSV form")
    verify()
}

object Sudoku {
    def main(args : Array[String]) : Unit = {
        val conf = new Conf(args)

        val sudoku = new Sudoku

        if (conf.input.isSupplied) {
            sudoku.importFile(conf.input())
        }
        else {
            sudoku.createRandomPuzzle()
        }

        println(sudoku)
    }
}