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


package org.tropic.sparkor.integration.benchmark.linprog.oscar

object ConstraintType extends Enumeration {
  type ConstraintType = Value
  val Equal, GreaterThan, LessThan = Value
}

import lpsolve._
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.tropic.sparkor.integration.benchmark.linprog.oscar.ConstraintType._

class LPSolver {

  /**
    * Prepends zeros to A matrix and c vector to satisfy lpsolve array size
    *
    * @param A parameter A of LP (matrix)
    * @param c parameter c of LP (vector)
    * @return A and c with zeros prepended
    */
  def addZeros(A: Array[Vector], c: Vector): (Array[Vector], Vector) = {
    var newA: Array[Vector] =  new Array[Vector](A.length)
    for(i <- A.indices)
      newA(i) = Vectors.dense(0.0 +: A(i).toArray)
    val newC: Vector = Vectors.dense(0.0 +: c.toArray)
    (newA, newC)
  }

  /**
    * Solves a linear optimization problem which can be expressed in the following form:
    * min c'x
    * subject to Ax = b (or >=, <=)
    * and x >= 0
    *
    * @param A n-by-m matrix A
    * @param b vector b with n elements
    * @param c vector c with m elements
    * @param constraintType constraint type in Ax [constraintType] b. (=, <= or >=)
    * @return Tuple (solution found (vector x), score)
    */
  def solve(A: Array[Vector], b: Vector, c: Vector, constraintType: ConstraintType): (Vector, Double) = {

    var solution: Vector = null
    val m = b.size
    val n = c.size
    var solver: LpSolve = null
    var score: Double = 0.0

    try {
      // Create a problem with n variables and 0 constraints
      solver = LpSolve.makeLp(0, n-1)

      solver.setVerbose(0)

      // add constraints
      constraintType match {
        case Equal  => for (i <- 0 until m) solver.addConstraint(A(i).toArray, LpSolve.EQ, b(i))
        case GreaterThan  => for (i <- 0 until m) solver.addConstraint(A(i).toArray, LpSolve.GE, b(i))
        case LessThan  => for (i <- 0 until m) solver.addConstraint(A(i).toArray, LpSolve.LE, b(i))
        case default  => println("Wrong constraint type: " + default.toString)
      }

      // set objective function
      solver.setObjFn(c.toArray)

      // solve the problem
      solver.solve()

      score = solver.getObjective
      solution = Vectors.dense(solver.getPtrVariables)

      // delete the problem and free memory
      solver.deleteLp();
    }
    catch {
      case e: LpSolveException => e.printStackTrace();
    }
    (solution, score)
  }
}
