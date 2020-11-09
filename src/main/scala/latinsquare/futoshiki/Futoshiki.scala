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

package latinsquare.futoshiki

import java.io.PrintWriter

import com.typesafe.scalalogging.Logger
import latinsquare.constraints.Ensemble
import latinsquare.{Cell, Point, Puzzle}

import scala.collection.mutable

class Futoshiki(maxValue: Int) extends Puzzle(maxValue) {
    private val logger = Logger[Futoshiki]
    private val rows = new mutable.ArrayBuffer[Ensemble]()
    private val columns = new mutable.ArrayBuffer[Ensemble]()
    private val relations = null

    def this() {
        this(5) // Default size for Times Futoshiki puzzles
    }

    // Initialisation

    for (x <- 1 to maxValue) {
        for (y <- 1 to maxValue) {
            val p = new Point(x,y)
            val cell = new Cell(maxValue, p)
            cells.put(p, cell)
        }
    }

    for (x <- 1 to maxValue) {
        val row = new Ensemble(maxValue, s"Row $x")
        rows.addOne(row)
        for (y <- 1 to maxValue) {
            addPointIntoUnit(x,y, row)
        }
    }

    for (y <- 1 to maxValue) {
        val column = new Ensemble(maxValue, s"Column $y")
        columns.addOne(column)
        for (x <- 1 to maxValue) {
            addPointIntoUnit(x,y, column)
        }
    }

    private def addPointIntoUnit(x : Int, y : Int, unit : Ensemble) : Unit = {
        val point = new Point(x, y)
        val cellOpt = cells.get(point)
        cellOpt match {
            case Some(cell) => unit.addCell(cell)
            case None => logger.error(s"Cannot find Cell for $point, should never happen")
        }
    }

    override def createRandomPuzzle(): Unit = ???

    override def importStrings(lines: Array[String]): Unit = ???

    override def writeStrings(writer: PrintWriter): Unit = ???
}
