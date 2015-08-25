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

// This class will be generated by `ck import td:sample_dataset` command:
object sample_dataset
{
  class Nasdaq extends Frame[Nasdaq]
  {
    val time = Column[Nasdaq, Long]("time")
    val symbol = Column[Nasdaq, String]("symbol")
    val open = Column[Nasdaq, Double]("open")
    val volume = Column[Nasdaq, String]("volume")
    val high = Column[Nasdaq, Double]("high")
    val low = Column[Nasdaq, Double]("low")
    val close = Column[Nasdaq, Double]("close")

    val * = Seq(time, symbol, open, volume, high, low, close)
  }

  val nasdaq = new Nasdaq
}

import org.scalatest.WordSpec
import sample_dataset._
import Schedule._

object Example
{
  // SELECT count(*) FROM nasdaq
  def dataCount = nasdaq.size

  // SELECT time, close FROM nasdaq WHERE symbol = 'APPL'
  def appleStock = nasdaq.filter(_.symbol is "APPL").select(_.time as "date", _.close)

  // You can use a raw SQL statement as well:
  def appleStockSQL = sql"SELECT time, close FROM ${nasdaq} where symbol = 'APPL'".as[Nasdaq]

  // SELECT time, close FROM nasdaq WHERE symbol = 'APPL' LIMIT 10
  //appleStock.limit(10).print

  //appleStockSQL.limit(5).print

  // time-column based filtering
  def recentAppleStock(scheduledTime:Schedule) = appleStock.between(scheduledTime, scheduledTime + 1.month)

  def multipleResults = {
    for(company <- Seq("YHOO", "GOOG", "MSFT")) yield {
      nasdaq.filter(_.symbol is company).selectAll
    }
  }

}

object RunnerExample {

  val td = new TDExecutor

  //Example.recentAppleStock(today).run(td)



}


class Example extends WordSpec {

  "Example" should {
    println(from(Seq(1, 2, 3)))
  }
}
