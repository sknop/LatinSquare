import sbt.util

name := "LatinSquare"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.1.0" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"

// Logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

scalacOptions += "-deprecation"

logLevel := util.Level.Warn

// [Required] Enable plugin and automatically find def main(args:Array[String]) methods from the classpath
enablePlugins(PackPlugin)
