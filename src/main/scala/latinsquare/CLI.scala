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

import java.io.File

import com.typesafe.scalalogging.Logger
import org.clapper.classutil.ClassFinder

import scala.util.Try
import scala.reflect.runtime.universe._

class CLI(plugins : List[String]) {
    for (plugin <- plugins) {
        println(plugin)
        newInstance(plugin) match  {
            case Some(p) => p.createRandomPuzzle(); println(p)
            case None => println("Cannot instantiate")
        }
    }

    def newInstance(className : String) : Option[Puzzle] = Try {
        if (className.endsWith("$")) {
            val m = runtimeMirror(getClass.getClassLoader)
            m.reflectModule(m.staticModule(className.init)).instance.asInstanceOf[Puzzle]
        }
        else {
            Class.forName(className).getDeclaredConstructor().newInstance().asInstanceOf[Puzzle]
        }
    }.toOption
}

object CLI {
    val logger = Logger[CLI]

    def main(args: Array[String]): Unit = {
        disableUnchecked()

        lookForStuff()
    }

    def disableUnchecked() : Unit =
        try {
            val unsafeClass = Class.forName("sun.misc.Unsafe")
            val field = unsafeClass.getDeclaredField("theUnsafe")
            field.setAccessible(true)
            val unsafe = field.get(null)

            val putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", classOf[Object], classOf[Long], classOf[Object])
            val staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", classOf[java.lang.reflect.Field])

            val loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger")
            val loggerField = loggerClass.getDeclaredField("logger")
            val offset = staticFieldOffset.invoke(unsafe, loggerField)
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null)
        }
        catch {
            case _ : Throwable => // ignored
        }

    def lookForStuff() : Unit = {
        val path : String = CLI.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
        logger.info("Path found " + path)

        val finder = ClassFinder(List(path).map(new File(_)))
        val classes = finder.getClasses()
        val classMap = ClassFinder.classInfoMap(classes)

        val plugins = ClassFinder.concreteSubclasses("latinsquare.Puzzle", classMap).collect { p => p.name }.toList

        println("Puzzles:")
        for (plugin <- plugins) {
            println("  " + plugin)
        }
    }
}