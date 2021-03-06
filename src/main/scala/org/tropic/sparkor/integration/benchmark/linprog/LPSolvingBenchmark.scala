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
import org.tropic.sparkor.integration.benchmark.linprog.oscar.{ConstraintType, LPSolver}
import org.tropic.sparkor.integration.benchmark.linprog.sparkor.SparkOrSolver
import org.tropic.sparkor.utils.MatrixUtils

object LPSolvingBenchmark extends App {

  /**
    * Solves LP with OscaR or spark-or, computes elapsed time and solution
    *
    * @param block function to solve the problem
    * @return (Solution, Total time in seconds)
    */
  def solveLinearProblem(block: => (Vector, Double)): (Vector, Double, Double) = {
    val t0 = System.nanoTime()
    val (solution, score) = block // call-by-name
    val t1 = System.nanoTime()
    (solution, score, (t1 - t0) / 1e9)
  }

  /**
    * Calculates the mean square error between the solution of OscaR and the solution of spark-or
    *
    * @param x1 first solution
    * @param x2 second solution
    * @return the mean square error
    */
  def mse(x1: Vector, x2: Vector): Double = {
    assert(x1.size == x2.size, "x1 and x2 must have the same size")
    val result = for(i <- 0 until x1.size) yield math.pow(x1(i) - x2(i), 2)
    var mse = 0.0
    for(i <- result.indices) mse += result(i)
    mse / result.length
  }



  val disableLpSolve: Boolean = args.length > 0 && args(0) == "--disable-lpsolve"

  val m = Array(5, 20, 50, 100, 250, 500)
  val n = Array(3, 10, 40, 90, 200, 400)
  val maxInt = 10

  var solution1: Vector = null
  var score1: Double = 0
  var t1: Double = 0

  val lp2 = new SparkOrSolver()

  for (i <- n.indices) {
    val param = new LPGeneration(m(i), n(i), maxInt)
    if (!disableLpSolve) {
      val lp = new LPSolver()
      val (newA, newC) = lp.addZeros(param.A, param.c)
      val res = solveLinearProblem {
        lp.solve(newA, param.b, newC, ConstraintType.GreaterThan)
      }
      solution1 = res._1
      score1 = res._2
      t1 = res._3
    }
    val (solution2, score2, t2) = solveLinearProblem {
      lp2.solve(MatrixUtils.arrayToMatrix(param.A), param.b, param.c, ConstraintType.GreaterThan)
    }

    // Print solution
    if (m(i) < 10 || n(i) < 10) {
      println("\nParameters:")
      println("A:")
      for (i <- param.A.indices) println(param.A(i))
      println("b:\n" + param.b)
      println("c:\n" + param.c)
      if (!disableLpSolve)
        println("\nSolution of lp_solve:" + solution1)
      println("Solution of spark-or:" + solution2)
    }
    println("****************************************************************")
    println("nbRows = " + n(i) + " & nbCols = " + m(i))
    if (!disableLpSolve)
      println("\nValue of objective function with lp_solve: " + score1)
    println("Value of objective function with spark-or: " + score2)
    if (!disableLpSolve)
      println("\nElapsed time of lp_solve: " + t1 + " s")
    println("Elapsed time of spark-or: " + t2 + " s")
    if (!disableLpSolve)
      println("\nMean square error : " + mse(solution1, solution2))
    println("****************************************************************")

  }
}
