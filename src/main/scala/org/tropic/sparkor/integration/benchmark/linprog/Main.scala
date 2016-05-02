package org.tropic.sparkor.integration.benchmark.linprog

import org.apache.spark.mllib.linalg.Vector
import org.tropic.sparkor.integration.benchmark.linprog.oscar.{ConstraintType, LinearProblem}
import org.tropic.sparkor.integration.benchmark.linprog.sparkor.LinearProblemSolver

object Main {

  /**
    * Solves LP with OscaR or spark-or, computes elapsed time and solution
    *
    * @param block function to solve the problem
    * @tparam R return type
    * @return (Solution, Total time in seconds)
    */
  def solveLinearProblem[R](block: => R): (R, Double) = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    (result, (t1 - t0) / 1000000000.0)
  }

  /**
    * Calculates the mean square error between the solution of OscaR and the solution of spark-or
    *
    * @param x1 first solution
    * @param x2 second solution
    * @return the mean square error
    */
  def mse(x1: Vector, x2: Vector): Double = {
    val result = for(i <- 0 until x1.size) yield math.pow(x1(i) - x2(i), 2)
    var mse = 0.0
    for(i <- result.indices) mse += result(i)
    mse / result.length
  }

  /**
    * Integration test to compare the solutions between OscaR and spark-or
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val lp = new LinearProblem()
    val lp2 = new LinearProblemSolver() //TODO: to change with real class of spark-or

    val m = 200
    val n = 500
    val maxInt = 10

    val param = new LPGeneration(m, n, maxInt)
    val (newA, newC) = lp.addZeros(param.A, param.c)
    val ((solution1, score1), t1) = solveLinearProblem{lp.solve(newA, param.b, newC, ConstraintType.GreaterThan)}
    val ((solution2, score2), t2) = solveLinearProblem{lp2.solve(param.A, param.b, param.c, ConstraintType.GreaterThan)} //TODO: to change with spark-or

    // Print solution
    if (m < 10 || n < 10) {
      println("\nParameters:")
      println("A:")
      for (i <- param.A.indices) println(param.A(i))
      println("b:\n" + param.b)
      println("c:\n" + param.c)
      println("\nSolution of lp_solve:" + solution1)
      println("Solution of spark-or:" + solution2)
    }
    println("\nValue of objective function with lp_solve: " + score1)
    println("Value of objective function with spark-or: " + score2)
    println("\nElapsed time of lp_solve: " + t1 + " s")
    println("Elapsed time of spark-or: " + t2 + " s")
    println("\nMean square error : " + mse(solution1, solution2))
  }
}
