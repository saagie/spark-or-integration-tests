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

  /**
    * Integration test to compare the solutions between OscaR and spark-or
    *
    * //@param args a option to choose solve with lpSolver (to entry something) or not
    */
  //def main(args: Array[String]): Unit = {

    val option: Boolean = args.length > 0

    val m = Array(5, 10, 50, 100, 210, 500)
    val n = Array(3, 18, 40, 90, 250, 500)
    val maxInt = 10

    var solution1: Vector = null
    var score1: Double = 0
    var t1: Double = 0

    val lp2 = new SparkOrSolver()

    for (i <- n.indices) {
      val param = new LPGeneration(m(i), n(i), maxInt)
      if (option) {
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
        if (option)
          println("\nSolution of lp_solve:" + solution1)
        println("Solution of spark-or:" + solution2)
      }
      println("****************************************************************")
      println("nbRows = " + n(i) + " & nbCols = " + m(i))
      if (option)
        println("\nValue of objective function with lp_solve: " + score1)
      println("Value of objective function with spark-or: " + score2)
      if (option)
        println("\nElapsed time of lp_solve: " + t1 + " s")
      println("Elapsed time of spark-or: " + t2 + " s")
      if (option)
        println("\nMean square error : " + mse(solution1, solution2))
      println("****************************************************************")

    }
  //}
}
