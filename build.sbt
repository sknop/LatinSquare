import sbt.util

name := "LatinSquare"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.2.0" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"

// Logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.9"

// Argument Parsing
libraryDependencies += "org.rogach" %% "scallop" % "3.5.0"

// Class Util
libraryDependencies += "org.clapper" %% "classutil" % "1.5.1"

scalacOptions += "-deprecation"

logLevel := util.Level.Warn

// assembly

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
}

// [Required] Enable plugin and automatically find def main(args:Array[String]) methods from the classpath
enablePlugins(PackPlugin)
