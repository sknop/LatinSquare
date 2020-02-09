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

import scala.collection.mutable
import scala.collection.immutable

class MarkUp(limit : Int) {
    private val bitSet = mutable.BitSet.empty

    // Constructor with BitMask
    def this(limit : Int, mask : Int) {
        this(limit)
        bitSet ++= immutable.BitSet.fromBitMaskNoCopy(Array[Long](mask))
    }

    private def this(limit : Int, origin : immutable.BitSet) {
        this(limit)
        bitSet ++= origin
    }

    private def this(limit : Int, origin : mutable.BitSet) {
        this(limit)
        bitSet ++= origin
    }

    // add a number
    def add(number : Int) : Unit = {
        require(number > 0 && number <= limit, s"Supplied number $number outside of ]0,$limit]")
        bitSet += number
    }

    // remove a number
    def clear(number : Int) : Unit = {
        require(number > 0 && number <= limit, s"Supplied number $number outside of ]0,$limit]")
        bitSet -= number
    }

    // check existance of a number, can be used as MarkUp()
    def apply(number : Int) : Boolean = {
        require(number > 0 && number <= limit, s"Supplied number $number outside of ]0,$limit]")
        bitSet(number)
    }

    def --(other : MarkUp) : MarkUp = {
        val result : immutable.BitSet = bitSet.toImmutable -- other.bitSet.toImmutable
        new MarkUp(limit, result)
    }

    def complement : MarkUp = {
        new MarkUp(limit, bitSet ^ MarkUp.allSet(limit).bitSet)
    }

    def iterator : Iterator[Int] = {
        complement.bitSet.iterator
    }

    // TODO might need to add flag to make readonly - or implement second class
    def toImmutable : MarkUp = new MarkUp(limit, bitSet)

    override def toString: String = {
        "MarkUp : " + bitSet.toString()
    }
}

object MarkUp {
    def allSet(limit : Int) : MarkUp = {
        new MarkUp(limit, (2 << limit) - 2)
    }
}
