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

package latinsquare.constraints

import latinsquare.exceptions.CellContentException
import latinsquare.{Cell, MarkUp}

import scala.collection.mutable.ArrayBuffer

abstract class AbstractConstraint protected (size : Int, position : String) extends Constraint {
    protected val _markup = new MarkUp(size)

    // checking for 0 is checking if the cell is empty - but markUp does not allow that value
    override def checkUpdate(value: Int): Boolean = value == 0 || ! _markup(value)

    @throws(classOf[CellContentException])
    override def update(oldValue: Int, newValue : Int): Unit = {
        if (oldValue != 0) {
            _markup.clear(oldValue)
        }

        if (newValue != 0) {
            if (_markup(newValue))
                throw new CellContentException(s"Value $newValue already exists in $this")

            _markup.add(newValue)
        }
    }

    override def markup: MarkUp = _markup.toImmutable

    override def toString : String = s"$position (${_markup.complement})"
}
