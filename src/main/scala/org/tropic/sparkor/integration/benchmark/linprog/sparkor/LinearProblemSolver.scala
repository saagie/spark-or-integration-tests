package org.tropic.sparkor.integration.benchmark.linprog.sparkor

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.tropic.sparkor.integration.benchmark.linprog.oscar.ConstraintType._

import scala.util.Random

// TODO: to change with real class of spark-or
class LinearProblemSolver {
  def solve(A: Array[Vector], b: Vector, c: Vector, constraintType: ConstraintType): (Vector, Double)  = {
    var solution = Vectors.dense(Array.fill(c.size)(Random.nextInt(10).toDouble))
    var score = 10.0
    (solution, score)
  }
}
