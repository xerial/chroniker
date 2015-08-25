package xerial.chroniker

/**
 *
 */

class Frame[A] {

  def limit(rows:Int) : Frame[A] = limit(rows, 0)
  def limit(rows:Int, offset:Int) : Frame[A] = LimitOp(this, rows, offset)

  def select1 : Option[Single[A]] = _
  def select(col1: (A => Column[A, _])*) : Frame[A] = _
  def selectAll : Frame[A] = _
  def filter(condition:A => Cond[A]) : Frame[A] = _

  def orderBy() : Frame[A] = _

  def size: Single[Int] = _

  def print = _

  def between(from:Schedule, to:Schedule) : Frame[A] = _

  def as[A] : Frame[A] = _

  def run(implicit executor:Executor) = _
}

trait Executor
class TDExecutor extends Executor


trait Single[Int]

trait Cond[A]
//case class Eq[A](other:Col[_]) extends Cond[A]
//case class EqExpr[A](cond:Col[A] => Boolean) extends Cond[A]


case class LimitOp[A](input:Frame[A], rows:Int, offset:Int) extends Frame[A]
case class FilterOp[A](input:Frame[A], cond:A => Boolean) extends Frame[A]




