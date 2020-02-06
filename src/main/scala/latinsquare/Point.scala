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

import latinsquare.exceptions.IllegalCellPositionException

case class Point(x : Int, y : Int) extends Ordered[Point] {
  override def toString: String = {
    String.format("(%d,%d)",x,y)
  }

  override def compare(that: Point): Int = {
    if (x == that.x)
      y - that.y
    else
      x - that.x
  }
}

object Point {
  def createChecked(x : Int, y : Int, min : Int, max : Int) : Point  = {
    if (x < min) throw new IllegalCellPositionException(String.format("%d less than %d", x, min))
    if (x > max) throw new IllegalCellPositionException(String.format("%d larger than %d", x, max))
    if (y < min) throw new IllegalCellPositionException(String.format("%d less than %d", y, min))
    if (y > max) throw new IllegalCellPositionException(String.format("%d larger than %d", y, max))

    new Point(x, y)
  }
}