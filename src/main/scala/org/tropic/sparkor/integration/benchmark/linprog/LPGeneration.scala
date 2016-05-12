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


package org.tropic.sparkor.integration.benchmark.linprog

import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors

import scala.util.Random

/**
  * Creates parameters A (Matrix), b (Vector) and c (Vector) with random numbers between 0 and maxInt.
  *
  * @param m numbers of cols of parameter A or length of parameter c
  * @param n numbers of rows of parameter A or length of parameter b
  * @param maxInt maximum value of coefficients
  */
class LPGeneration(m: Int = 4, n: Int = 2, maxInt: Int = 10) {
  private val a = for(i <- 0 until n) yield Vectors.dense(Array.fill(m)(Random.nextInt(maxInt).toDouble))
  val A: Array[Vector] = a.toArray
  val b: Vector = Vectors.dense(Array.fill(n)(Random.nextInt(maxInt).toDouble))
  val c: Vector = Vectors.dense(Array.fill(m)((Random.nextInt(maxInt) + 1).toDouble))
}

