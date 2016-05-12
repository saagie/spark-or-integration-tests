/*
 *   Spark-OR version 0.0.1
 *
 *   Copyright 2016 Saagie
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
