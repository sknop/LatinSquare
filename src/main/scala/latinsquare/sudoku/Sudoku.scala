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

object Sudoku {
    def main(args : Array[String]) : Unit = {
        val sudoku = new Sudoku

        sudoku.createRandomPuzzle()

        println(sudoku)
    }
}