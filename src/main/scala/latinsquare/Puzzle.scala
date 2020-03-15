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

package latinsquare

import java.io.FileNotFoundException

import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.io.Source

abstract class Puzzle(val maxValue : Int) {
    private val logger = Logger[Puzzle]
    val cells = new mutable.HashMap[Point, Cell]()

    def isSolved : Boolean = ! cells.exists(_._2.isEmpty)

    def createRandomPuzzle() : Unit

    def importFile(fileName : String) : Unit = {
        try {
            val source = Source.fromFile(fileName)
            val lines = source.getLines().toArray

            importStrings(lines)

            source.close()
        }
        catch  {
            case e: FileNotFoundException => println("Cannot find file " + fileName); throw e
        }
    }

    def importStrings(lines : Array[String]) : Unit

    def dimension : Int

    def reset() : Unit = {
        cells.values.foreach(_.reset())
    }

    def solveBruteForce : Boolean = {
        solveRecursive(sortedEmptyCells)
    }

    def isUnique : Int = {
        uniqueRecursive(sortedEmptyCells, 0)
    }

    def value(x : Int, y : Int) : Int = {
        value(new Point(x,y))
    }

    def value(p : Point) : Int = {
        cells(p).value
    }

    // protected methods (mostly for drawing)

    protected def bigBorder(builder : mutable.StringBuilder, size : Int) : Unit = {
        builder.append("  +")
        builder.append("-" * (2 * (size * size + size) - 1))
        builder.append("+")
    }

    protected def littleBorder(builder : mutable.StringBuilder, size : Int) : Unit = {
        builder.append("  |")
        builder.append("-" * (size * 2 + 1))
        for (_ <- 1 until size) {
            builder.append("+")
            builder.append("-" * (size * 2 + 1))
        }
        builder.append("|")
    }

    protected def valueAsString(x : Int, y : Int) : String = {
        val v = value(x, y)

        NumberConverter.valueAsString(v)
    }

    // private methods

    private def sortedEmptyCells : List[Cell] = {
        cells.values.
          filter(_.isEmpty).
          toList.
          sortBy(_.markUp.cardinality)
    }

    private def solveRecursive(empties: List[Cell]) : Boolean = {
        if (empties.isEmpty) return true

        val head : Cell = empties.head
        val tail : List[Cell] = empties.tail

        for (i <- head.iterator) {
            head.value = i

            val sortedTail = tail.sortBy(_.markUp.cardinality)

            if (solveRecursive(sortedTail))
                return true

            head.reset()
        }

        false
    }

    private def uniqueRecursive(empties: List[Cell], solutions : Int) : Int = {
        if (empties.isEmpty)
            return solutions + 1

        val head : Cell = empties.head
        val tail : List[Cell] = empties.tail
        var result = solutions

        for (i <- head.iterator) {
            head.value = i

            val sortedTail = tail.sortBy(_.markUp.cardinality)
            result = uniqueRecursive(sortedTail, result)
            if (result > 1) {
                head.reset() // need to reset, otherwise puzzle is solved
                return result
            }

            head.reset()
        }

        result
    }
}

object Puzzle {
    val Front = " |"
    val Section = " %s %s %s |"
}