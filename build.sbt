name := "chroniker"
description := "A framework for simplifying batch job pipelines"
organization := "org.xerial"
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "org.xerial" % "xerial-lens" % "3.3.6",
  "org.scala-lang" % "scalap" % scalaVersion.value,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
