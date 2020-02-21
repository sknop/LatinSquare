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

import latinsquare.exceptions.IllegalCellPositionException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PointTest extends AnyFlatSpec with Matchers {
  "A Point" should "be equal to another point if x and y match" in {
    val p1 : Point = new Point(1,1)
    val p2 : Point = new Point(1,1)
    val p3 : Point = new Point(1,2)

    p1 == p2 should be (true)
    p1 == p3 should be (false)
  }

  it should "have the same hashcode if x and y match" in {
    val p1 : Point = new Point(1,1)
    val p2 : Point = new Point(1,1)
    val p3 : Point = new Point(1,2)

    p1.hashCode() == p2.hashCode() should be (true)
    p1.hashCode() == p3.hashCode() should be (false)
  }

  it should "have the correct string representation" in {
    val p1 = new Point(1,2)

    p1.toString should be ("(1,2)")
  }

  it should "be ordered by y and x" in {
    val p1 : Point = new Point(1,1)
    val p2 : Point = new Point(1,2)
    val p3 : Point = new Point(2,1)

    p1.compare(p2) should be (-1)
    p1.compare(p2) should be (-1)
    p2.compare(p3) should be (-1)

    p2.compare(p1) should be (1)
  }

  it should "be created without exceptions" in {
    val p1 = Point.createChecked(1,1,1,2)
  }

  it should "throw exception if exceeding its boundaries" in {
    a [IllegalCellPositionException] should be thrownBy {
      val p1 = Point.createChecked(2,1,1,1)
    }
  }
}
