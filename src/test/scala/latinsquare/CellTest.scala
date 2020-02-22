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

import latinsquare.exceptions.CellContentException
import org.scalatest.OneInstancePerTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CellTest extends AnyFlatSpec
  with Matchers
  with OneInstancePerTest {
    behavior of "A Cell"

    val cell = new Cell(9,1,2)

    it should "be empty on creation" in {
        cell.isEmpty should be (true)
    }

    it should "be writable on creation" in {
        cell.readOnly should be (false)
    }

    it should "accept a value below the limit" in {
        cell.value = 3
    }

    it should "throw exception if setting a value larger than limit" in {
        a [CellContentException] should be thrownBy {
            cell.value = 10
        }
    }

    it should "have the correct String representation" in {
        cell.value = 3

        cell.toString should be ("(1,2):3")
    }

    it should "reset without errors" in {
        cell.value = 3

        cell.value should be (3)

        cell.reset()

        cell.value should be (0)
    }

    it should "be able to be set to readonly and back" in {
        cell.value = 3

        cell.readOnly should be (false)

        cell.readOnly = true

        cell.readOnly should be (true)

        cell.readOnly = false

        cell.readOnly should be (false)
    }
}
