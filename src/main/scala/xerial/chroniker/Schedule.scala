package xerial.chroniker

trait Schedule

case class RecurringSchedule(since:Option[_], until:Option[_], ) extends Schedule
case class FixedSchedule extends Schedule

trait DateUnit
object Hour extends DateUnit
object Day extends DateUnit
object Week extends DateUnit
object Month extends DateUnit
object Year extends DateUnit

case class Repeat(duration:Int, unit:DateUnit)
case class RepeatInWeek()

/**
 *
 */
object Schedule
{
  implicit class toDate(v:Int) {
    // def hour =
  }

  def everyHour = Repeat(1, Hour)
  def everyDay = Repeat(1, Day)
  def everyWeekDay = _
  def everyWeek = Repeat(1, Week)
  def everyMonth = Repeat(1, Month)
  def everyYear = Repeat(1, Year)

  def every(d:Day*) = _

  def endOfHour = _
  def endOfDay = _
  def endOfWeek = _
  def endOfMonth = _
  def endOfYear = _

  def lastYear = _
  def lastMonth = _
  def lastWeek = _
  def yesterday = _
  def today = _
  def tomorrow = _
  def nextWeek = _
  def nextMonth = _
  def nextYear = _

  sealed trait CalendarDate

  sealed trait Day extends CalendarDate
  object Monday extends Day
  object Tuesday extends Day
  object Wednesday extends Day
  object Thursday extends Day
  object Friday extends Day
  object Saturday extends Day
  object Sunday extends Day

  sealed trait Month extends CalendarDate
  object January extends Month
  object February extends Month
  object March extends Month
  object April extends Month
  object May extends Month
  object June extends Month
  object July extends Month
  object August extends Month
  object September extends Month
  object October extends Month
  object November extends Month
  object December extends Month

}
