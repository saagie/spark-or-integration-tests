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
    //println(s.getVector)
  }

  def solvingStoppedCallback(s: Solution, solver: Solver): Unit = {
    //println("Final solution")
    //println(s.getVector)
    //println("Score")
    //println(solver.getScore)
    solution = s
  }

  def solve(A: Matrix, b: Vector, c: Vector, constraintType: ConstraintType): (Vector, Double)  = {
    var iterInterval = 5
    val conf = new SparkConf().setAppName("sparkor").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val solver = new InteriorPointSolver(sc)

    val problem = new LinearOptimizationProblem(A, b, c, ConstraintType.GreaterThan)

    solver.setProblem(problem)
    solver.setNewSolutionFoundCallback(iterInterval, solutionFoundCallback)
    solver.setSolvingStoppedCallback(solvingStoppedCallback)
    solver.solve()

    var score = solver.getScore
    sc.stop()
    (solution.getVector, score)
  }
}
