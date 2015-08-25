/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xerial.chroniker

/**
 *
 */
class Frame[A] {

  def limit(rows:Int) : Frame[A] = limit(rows, 0)
  def limit(rows:Int, offset:Int) : Frame[A] = LimitOp(this, rows, offset)

  def select1 : Option[Single[A]] = null
  def select(col1: (A => Column[A, _])*) : Frame[A] = null
  def selectAll : Frame[A] = null
  def filter(condition:A => Cond[A]) : Frame[A] = null

  def orderBy() : Frame[A] = null

  def size: Single[Int] = null

  def print = null

  def between(from:Schedule, to:Schedule) : Frame[A] = null

  def as[A] : Frame[A] = null

  def run(implicit executor:Executor) = null

}


case class InputFrame[A](context:FContext, input:Seq[A]) extends Frame[A]
case class FrameRef[A](context:FContext) extends Frame[A]

/**
 *
 */
case class Column[Table, ColType](name:String)
{
  // TODO
  def is[A](other:A) : Cond[Table] = null

  def as(newAlias:String) : Column[Table, ColType] = null

}

trait Single[Int]

trait Cond[A]
//case class Eq[A](other:Col[_]) extends Cond[A]
//case class EqExpr[A](cond:Col[A] => Boolean) extends Cond[A]


case class LimitOp[A](input:Frame[A], rows:Int, offset:Int) extends Frame[A]
case class FilterOp[A](input:Frame[A], cond:A => Boolean) extends Frame[A]


