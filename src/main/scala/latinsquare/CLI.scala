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

import org.clapper.classutil.{ClassFinder, ClassInfo}

class CLI {

}

object CLI {
    def main(args: Array[String]): Unit = {
        val finder = ClassFinder()
        val classes = finder.getClasses()
        val classMap = ClassFinder.classInfoMap(classes)

        val plugins = ClassFinder.concreteSubclasses("latinsquare.Puzzle", classMap)
        println("Puzzles: ")
        plugins.foreach(println)

        val constraints = ClassFinder.concreteSubclasses("latinsquare.unit.Constraint", classMap)
        println("Constraints:")

        for (c : ClassInfo <- constraints) {
            println(c)
            println("Interfaces : " + c.interfaces)
            println("Superclass : " + c.superClassName)
        }
    }
}