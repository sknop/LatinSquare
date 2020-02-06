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

import latinsquare.exceptions.CellContentException
import latinsquare.unit.{AbstractConstraint, Constraint}

import scala.collection.mutable.ArrayBuffer

class Cell(var limit: Int, var location: Point) {

    private var _value : Int = 0
    private var _readOnly: Boolean = false
    private val _constraints = new ArrayBuffer[Constraint]

    def this(limit: Int, x: Int, y: Int) {
        this(limit, Point(x, y))
    }

    def readonly: Boolean = _readOnly

    def empty: Boolean = (_value == 0)

    def value : Int = _value

    def readonly_(flag : Boolean) : Unit = {
        if (flag && _value != 0)
            _readOnly = true
        else if (_value != 0)
            _readOnly = false
    }

    def reset : Unit = {
        _readOnly = false
        setValue(0)
    }

    def markUp : MarkUp = {
        var result : MarkUp = MarkUp.allSet(limit)

        _constraints.foldLeft(result)((x,y) => x -- y.markup)
    }

    @throws(classOf[CellContentException])
    def setValue(value: Int): Unit = {
        if (_readOnly)
            throw new CellContentException(this.toString + " is read only")

        if (value > limit)
            throw new CellContentException("Value " + value + " is larger than " + limit)

        // first, check contraints without changing them
        // foldLeft, because the type changes from latinsquare.unit.Constraint to Boolean
        if (! _constraints.foldLeft(true)(_ & _.checkUpdate(value)))
            throw new CellContentException(s"Value $value already in constraints")

        // then set constraints
        _constraints.foreach(_.update(this._value, value))

        this._value = value
    }

    def addConstraint(constraint: Constraint) =
        _constraints += constraint

    def removeConstraint(constraint: Constraint) =
        _constraints -= constraint

    override def toString: String = location.toString + ":" + getValueAsString

    def getValueAsString: String = {
        val c: Int = if (_value < 10) ('0' + _value) else ('A' + _value - 10)

        c.toChar.toString
    }

}
