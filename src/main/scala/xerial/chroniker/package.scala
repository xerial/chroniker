package xerial

/**
 *
 */
package object chroniker
{
  implicit class SqlContext(val sc:StringContext) extends AnyVal {
    def sql[T](args:Any*) : Table[T] = {
      null
    }
  }

  def SCHEDULED_TIME : String = "" // TODO


  implicit class Duration(n:Int) {
    def month : String = "" // TODO

  }


}


