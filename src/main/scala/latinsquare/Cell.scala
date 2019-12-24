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
import unit.Constraint

import scala.collection.mutable.ArrayBuffer

class Cell(var limit: Int, var location: Point) {

    private var value: Int = 0
    private var readOnly: Boolean = false
    private val constraints = new ArrayBuffer[Constraint]

    def this(limit: Int, x: Int, y: Int) {
        this(limit, Point(x, y))
    }

    def isReadOnly: Boolean = readOnly

    def isEmpty: Boolean = (value == 0)

    def makeReadOnly : Unit = {
        if (value != 0)
            readOnly = true
    }

    def makeWritable : Unit = {
        if (value != 0) {
            readOnly = false
        }
    }

    @throws(classOf[CellContentException])
    def setValue(value: Int): Unit = {
        if (readOnly)
            throw new CellContentException(this.toString + " is read only")
        else if (value > limit)
            throw new CellContentException("Value " + value + " is larger than " + limit)
        else {
            // first, check contraints without changing them
            constraints.foreach(_.checkUpdate(value))

            // then set constraints
            constraints.foreach(_.update(value))

            this.value = value
        }
    }

    def addConstraint(constraint: Constraint) =
        constraints += constraint

    def removeConstraint(constraint: Constraint) =
        constraints -= constraint

    override def toString: String = location.toString + ":" + getValueAsString

    def getValueAsString: String = {
        val c: Int = if (value < 10) ('0' + value) else ('A' + value - 10)

        c.toChar.toString
    }

}
