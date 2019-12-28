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

import org.scalatest.OneInstancePerTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MarkUpTest extends AnyFlatSpec with Matchers with OneInstancePerTest {
    behavior of "A MarkUp"

    val markUp = new MarkUp(9)

    it should "behave like a BitSet for adding" in {
        markUp(1) should be (false)

        markUp.add(1)

        markUp(1) should be (true)

        markUp.clear(1)

        markUp(1) should be (false)
    }

    it should "have a working complement" in {
        markUp.add(1)
        markUp.add(2)

        val complement = markUp.complement

        complement(1) should be (false)
        complement(3) should be (true)
    }

    it should "throw an exception if supplied numbers outside of range" in {
        markUp.add(9)

        a [IllegalArgumentException] should be thrownBy {
            markUp.add(10)
        }
    }
}