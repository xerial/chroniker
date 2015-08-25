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


case class RecurringSchedule(since:Option[Schedule], until:Option[Schedule]) extends Schedule
case class FixedSchedule() extends Schedule

trait DateUnit
object Hour extends DateUnit
object Day extends DateUnit
object Week extends DateUnit
object Month extends DateUnit
object Year extends DateUnit

case class Repeat(duration:Int, unit:DateUnit)
case class RepeatInWeek()

class Schedule {
  def +(other:Schedule) : Schedule = null
}

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
  def everyWeekDay = throw UNDEFINED
  def everyWeek = Repeat(1, Week)
  def everyMonth = Repeat(1, Month)
  def everyYear = Repeat(1, Year)

  def every(d:Day*) = throw UNDEFINED

  def endOfHour = throw UNDEFINED
  def endOfDay = throw UNDEFINED
  def endOfWeek = throw UNDEFINED
  def endOfMonth = throw UNDEFINED
  def endOfYear = throw UNDEFINED

  def lastYear = throw UNDEFINED
  def lastMonth = throw UNDEFINED
  def lastWeek = throw UNDEFINED
  def yesterday = throw UNDEFINED
  def today : Schedule = throw UNDEFINED
  def tomorrow = throw UNDEFINED
  def nextWeek = throw UNDEFINED
  def nextMonth = throw UNDEFINED
  def nextYear = throw UNDEFINED

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
