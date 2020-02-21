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

import scala.collection.mutable

abstract class Puzzle(val maxValue : Int) {
    val cells = new mutable.HashMap[Point, Cell]()

    def isSolved : Boolean = ! cells.exists(_._2.empty)

    def createRandomPuzzle : Unit

    def reset : Unit = {
        cells.values.foreach(_.reset)
    }

    def solveBruteForce : Boolean = {
        solveRecursive(emptyCells)
    }

    def isUnique : Int = {
        uniqueRecursive(emptyCells, 0)
    }

    // private methods

    private def emptyCells : mutable.Stack[Cell] = {
        mutable.Stack.from(cells.values.filter(_.empty).toList.sortBy(_.location))
    }

    private def solveRecursive(tail: mutable.Stack[Cell]) : Boolean = {
        if (tail.size == 0) true

        val head : Cell = tail.pop

        for (i <- head.iterator) {
            head.setValue(i)

            val sortedTail = tail.sortBy(_.markUp.cardinality)

            if (solveRecursive(sortedTail))
                return true

            head.reset
        }

        tail.push(head)
        false
    }

    private def uniqueRecursive(tail: mutable.Stack[Cell], solutions : Int) : Int = {
        if (tail.size == 0)
            return solutions + 1

        val head : Cell = tail.pop

        for (i <- head.iterator) {
            head.setValue(i)

            val sortedTail = tail.sortBy(_.markUp.cardinality)
            val result = uniqueRecursive(sortedTail, solutions)
            if (result > 1) {
                head.reset // need to reset, otherwise puzzle is solved
                return result
            }

            head.reset
        }

        tail.push(head) // back to front of queue

        solutions
    }

}
