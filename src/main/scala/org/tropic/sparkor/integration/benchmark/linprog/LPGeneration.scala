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

