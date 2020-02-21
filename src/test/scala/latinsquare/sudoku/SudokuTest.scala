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

import latinsquare.Point
import org.scalatest.OneInstancePerTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SudokuTest extends AnyFlatSpec  with Matchers with OneInstancePerTest {
    behavior of "A Sudoku Puzzle"

    val sudoku = new Sudoku

    it should "be empty when constructed" in {
        for (cell <- sudoku.cells.values) {
            cell.empty should be (true)
        }
    }

    it should "accept a single value and show the correct markup" in {
        val point : Point = new Point(1, 1)
        val cell = sudoku.cells(point)

        cell.setValue(1)

        println(cell.markUp)
        for (i <- cell.iterator)
            println(i)
    }
}
