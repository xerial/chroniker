package xerial.chroniker

/**
 *
 */
trait Table[T]
{
  def size : Long

  def select(col:(T => Column[_])*) : Table[T]
  def filter(cond:T => Boolean) : Table[T]

  def limit(num:Int) : Table[T]

  def print : Unit

  def between(from:String, to:String) : Table[T]

  def as[A] : Table[A]

  def run(implicit executor:Executor)
}


trait Executor
class TDExecutor extends Executor