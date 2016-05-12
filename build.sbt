name := "spark-or-integration-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "1.6.1","org.apache.spark" %% "spark-mllib" % "1.6.1")

unmanagedJars in Compile += file("lib/lpsolve55j.jar")
unmanagedJars in Compile += file("spark-or.jar")
