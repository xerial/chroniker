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
package xerial

import java.io.File

import scala.language.experimental.macros

/**
 *
 */
package object chroniker
{
  import FrameMacros._

  implicit class SqlContext(val sc:StringContext) extends AnyVal {
    def sql(args:Any*) : RawSQL = macro mSQL
  }

  private[chroniker] val UNDEFINED : Nothing = throw new UnsupportedOperationException("undefined")

  implicit class Duration(n:Int) {
    def month : Duration = UNDEFINED
    def seconds : Duration = UNDEFINED
  }

  def from[A](in:Seq[A]) : InputFrame[A] = macro mNewFrame[A]
  def fromFile[A](in:File) : FileInput[A] = macro mFileInput[A]

}


