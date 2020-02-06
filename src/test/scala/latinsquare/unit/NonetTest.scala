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

package latinsquare.unit

import latinsquare.exceptions.CellContentException
import latinsquare.Cell
import org.scalatest.OneInstancePerTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class NonetTest extends AnyFlatSpec with Matchers with OneInstancePerTest {
    behavior of "A Nonet"

    val nonet = new Nonet("Test")

    it should "be empty to start with" in {
        nonet.checkUpdate(1) should be (true)
        nonet.checkUpdate(9) should be (true)
    }

    it should "accept an update and then report it " in {
        nonet.update(0,1)
        nonet.checkUpdate(1) should be (false)
        nonet.checkUpdate(2) should be (true)
    }

    it should "accept cells" in {
        val testNonet = new Nonet("Somewhere")

        val x = 1
        for (y <- 1 to 9) {
            val cell : Cell = new Cell(9, x, y)
            testNonet.addCell(cell)
        }
        a [IllegalArgumentException] should be thrownBy {
            val cell : Cell = new Cell(9,1,10)
            testNonet.addCell(cell)
        }
    }

    it should "accept cell updates" in {
        val testNonet = new Nonet("Somewhere")
        val cells = new Array[Cell](9)

        val x = 3
        for (y <- 1 to 9) {
            val cell : Cell = new Cell(9, x, y)

            cells(y-1) = cell
            testNonet.addCell(cell)
        }

        for (i <- 1 to 9) {
            cells(i-1).setValue(i)
        }

        a [CellContentException] should be thrownBy {
            (cells(0).setValue(2))
        }
    }

    it should "clear cells successfully" in {
        val testNonet = new Nonet("Somewhere")
        val cells = new Array[Cell](9)

        val x = 3
        for (y <- 1 to 9) {
            val cell : Cell = new Cell(9, x, y)

            cells(y-1) = cell
            testNonet.addCell(cell)
        }

        for (i <- 1 to 9) {
            cells(i-1).setValue(i)
        }

        for (i <- 1 to 9) {
            cells(i-1).reset
        }

        for (i <- 1 to 9) {
            cells(i-1).setValue(i)
        }
    }

}
