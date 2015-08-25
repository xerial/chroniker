package xerial.chroniker

/**
 *
 */
case class Column[Table, ColType](name:String)
{
  // TODO
  def is[A](other:A) : Cond[Table] = _
}
