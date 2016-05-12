name := "spark-or-integration-test"

version := "1.0"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "1.6.1","org.apache.spark" %% "spark-mllib" % "1.6.1","org.scalatest" %% "scalatest" % "2.2.4" % "test")

unmanagedJars in Compile += file("lib/lpsolve55j.jar")
unmanagedJars in Compile += file("lib/spark-or.jar")

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
}
