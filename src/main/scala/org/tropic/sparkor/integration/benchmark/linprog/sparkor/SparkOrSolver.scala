package org.tropic.sparkor.integration.benchmark.linprog.sparkor

import org.apache.spark.mllib.linalg.{Matrix, Vector}
import org.tropic.sparkor.integration.benchmark.linprog.oscar.ConstraintType._
import org.apache.spark.{SparkContext, SparkConf}
import org.tropic.sparkor.core.{Solver, Solution}
import org.tropic.sparkor.linprog.{ConstraintType, LinearOptimizationProblem, InteriorPointSolver}


class SparkOrSolver {
  var solution: Solution = null

  def solutionFoundCallback(iter: Int, s: Solution, solver: Solver) {
    println("Solution at iteration " + iter)
    println(s.getVector)
  }

  def solvingStoppedCallback(s: Solution, solver: Solver): Unit = {
    println("Final solution")
    println(s.getVector)
    println("Score")
    println(solver.getScore)
    solution = s
  }

  def solve(A: Matrix, b: Vector, c: Vector, constraintType: ConstraintType): (Vector, Double)  = {
    var iterInterval = 2
    val conf = new SparkConf().setAppName("sparkor").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val solver = new InteriorPointSolver(sc)

    val problem = new LinearOptimizationProblem(A, b, c, ConstraintType.GreaterThan)

    solver.setProblem(problem)
    solver.setNewSolutionFoundCallback(iterInterval, solutionFoundCallback)
    solver.setSolvingStoppedCallback(solvingStoppedCallback)
    println("appel solve")
    solver.solve()

    var score = solver.getScore
    sc.stop()
    (solution.getVector, score)
  }
}
