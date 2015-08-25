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
import scala.language.experimental.macros

import FrameMacros._

/**
 *
 */
trait Frame[A] {

  def inputs : Seq[Frame[_]]

  def limit(rows:Int) : Frame[A] = macro mLimit[A]
  def limit(rows:Int, offset:Int) : Frame[A] = macro mLimitWithOffset[A]

  def select1 : Option[Single[A]] = null
  def select(cols: (A => Column[A, _])*) : Frame[A] = macro mSelect[A]
  def selectAll : Frame[A] = null
  def filter(condition:A => Cond[A]) : Frame[A] = macro mFilter[A]

  def orderBy() : Frame[A] = null

  def size: Single[Int] = null

  def print = null

  def between(from:Schedule, to:Schedule) : Frame[A] = null

  def as[A] : Frame[A] = macro mAs[A]

  def run(implicit executor:Executor) = null

//  override def toString = {
//    toString(0)
//  }
//
//  def toString(indentLevel:Int) : String = {
//    val s = new StringBuilder
//    s.append(this.getClass.getSimpleName)
//    s.append("\n")
//    this match {
//      case p:Product =>
//        val indent = (0 until indentLevel).map(_ => " ").mkString
//        val st = for(param <- p.productIterator) yield {
//          param match {
//            case f: Frame[_] => f.toString(indentLevel + 1)
//            case other => s"${indent}- ${other}"
//          }
//        }
//        s.append(st.mkString("\n"))
//      case _ =>
//    }
//    s.result()
//  }
//

}


case class InputFrame[A](context:FContext, data:Seq[A]) extends Frame[A] {
  def inputs = Seq.empty
}
case class FrameRef[A](context:FContext) extends Frame[A] {
  def inputs = Seq.empty
}

case class RawSQL(context:FContext, sc:Any, args:Seq[Any]) extends Frame[Any] {
  // FIXME to track dependencies
  def inputs = args.collect{case f:Frame[_] => f}
}

case class CastAs[A](context:FContext, input:Frame[_]) extends Frame[A] {
  def inputs = Seq(input)
}

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

case class LimitOp[A](context:FContext, input:Frame[A], rows:Int, offset:Int) extends Frame[A] {
  def inputs = Seq(input)
}
case class FilterOp[A](context:FContext, input:Frame[A], cond:A => Cond[A]) extends Frame[A] {
  def inputs = Seq(input)
}
case class ProjectOp[A](context:FContext, input:Frame[A], col:Seq[A => Column[A, _]]) extends Frame[A] {
  def inputs = Seq(input)
}

object SQLHelper {

  def templateString(sc:StringContext) = {
    val b = new StringBuilder
    for(p <- sc.parts) {
      b.append(p)
      b.append("${}")
    }
    b.result()
  }


  private def isFrameType[A](cl: Class[A]): Boolean = classOf[Frame[_]].isAssignableFrom(cl)

  def frameInputs(args:Seq[Any]) = {
    val b = Seq.newBuilder[Frame[_]]
    for((a, index) <- args.zipWithIndex) {
      if(isFrameType(a.getClass)) {
        b += a.asInstanceOf[Frame[_]]
      }
    }
    b.result
  }

}

