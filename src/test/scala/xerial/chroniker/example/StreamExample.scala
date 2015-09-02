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
package xerial.chroniker.example

import xerial.chroniker
import xerial.chroniker.{RootFrame, Column}
import chroniker._

class Tweet extends RootFrame[Tweet] {
  val retweetCount = Column[Tweet, Long]("count")
  val timeZone = Column[Tweet, String]("timezone")
}

/**
 *
 */
object StreamExample {

  def tweet = new Tweet

  // event time, processing time difference

  def last5Tweets =
    tweet
      .fixedSize(5)
      .filter(_.retweetCount >= 10).sum(_.retweetCount)

  def slidingWindowOf60sec =
    tweet
      .fixedDuration(60 seconds)
      .aggregate(_.retweetCount.max, _.retweetCount.min, _.retweetCount.avg)

  def groupByTimeZone =
    tweet
     .fixedDuration(30 seconds)
     .aggregate(_.timeZone)
     .select(_.timeZone, _.retweetCount.sum)




}


