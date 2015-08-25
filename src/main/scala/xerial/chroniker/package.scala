package xerial

/**
 *
 */
package object chroniker
{
  implicit class SqlContext(val sc:StringContext) extends AnyVal {
    def sql[T](args:Any*) : Frame[T] = {
      null
    }
  }

  implicit class Duration(n:Int) {
    def month : Schedule = _ // TODO

  }


  def from[A](frame:Frame[A]) : Frame[A] = _

}


