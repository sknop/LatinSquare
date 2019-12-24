/*
 * ******************************************************************************
 *  * Copyright (c) 2019 Sven Erik Knop.
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
 *  *     2019 - Sven Erik Knop - initial API and implementation
 *  ******************************************************************************
 */

package latinsquare

import exceptions.CellContentException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CellTest extends AnyFlatSpec with Matchers {
    "A Cell" should "be empty on creation" in {
        var cell = new Cell(9, 1,1)

        cell.isEmpty should be (true)
    }

    it should "be writable on creation" in {
        var cell = new Cell(9, 1,1)

        cell.isReadOnly should be (false)
    }

    it should "accept a value below the limit" in {
        var cell = new Cell(9,1,2)

        cell.setValue(3)
    }

    it should "throw exception if setting a value larger than limit" in {
        val cell = new Cell(2,1,1)

        a [CellContentException] should be thrownBy {
            cell.setValue(3)
        }
    }
}
