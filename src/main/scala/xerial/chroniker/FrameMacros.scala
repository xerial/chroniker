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

import scala.reflect.macros.blackbox.Context
import scala.language.existentials
import scala.language.experimental.macros
import scala.reflect.runtime.{universe => ru}

import scala.language.experimental.macros

/**
 *
 */
object FrameMacros
{

  class MacroHelper[C <: Context](val c: C)
  {
    import c.universe._

    /**
     * Find a function/variable/class context where the expression is used
     * @return
     */
    def createFContext: c.Expr[FContext] =
    {
      // Find the enclosing method.
      val m = c.enclosingMethod
      val methodName = m match {
        case DefDef(mod, name, _, _, _, _) =>
          name.decodedName.toString
        case other =>
          "<constructor>"
      }


      val selfCl = c.Expr[AnyRef](This(typeNames.EMPTY))
      val vd = findValDef
      var parent: c.Expr[Option[String]] = reify {
        None
      }
      val vdTree = if (vd.isEmpty) {
        val nme = c.literal(c.internal.enclosingOwner.name.decodedName.toString)
        reify {
          Some(nme.splice)
        }
      }
      else {
        if (!vd.tail.isEmpty) {
          parent = reify {
            Some(c.literal(vd.tail.head.name.decodedName.toString).splice)
          }
        }
        val nme = c.literal(vd.head.name.decodedName.toString)
        reify {
          Some(nme.splice)
        }
      }
      //      val vdTree = vd match {
      //        case Some(v) =>
      //          val nme = c.literal(v.name.decodedName.toString)
      //          reify { Some(nme.splice)}
      //        case None =>
      //          reify { None }
      //      }

      val pos = c.enclosingPosition
      c.Expr[FContext](q"FContext($selfCl.getClass, $methodName, $vdTree, $parent, ${pos.source.path}, ${pos.line}, ${pos.column})")
    }

    // Find a target variable of the operation result by scanning closest ValDefs
    def findValDef: List[ValOrDefDef] =
    {

      def print(p: c.Position) = s"${p.line}(${p.column})"

      val prefixPos = c.prefix.tree.pos

      class Finder
              extends Traverser
      {

        var enclosingDef: List[ValOrDefDef] = List.empty
        var cursor: c.Position = null

        private def contains(p: c.Position, start: c.Position, end: c.Position) =
          start.precedes(p) && p.precedes(end)

        override def traverse(tree: Tree): Unit =
        {
          if (tree.pos.isDefined)
            cursor = tree.pos
          tree match {
            // Check whether the rhs of variable definition contains the prefix expression
            case vd@ValDef(mod, varName, tpt, rhs) =>
              // Record the start and end positions of the variable definition block
              val startPos = vd.pos
              super.traverse(rhs)
              val endPos = cursor
              if (contains(prefixPos, startPos, endPos)) {
                enclosingDef = vd :: enclosingDef
              }
            case other =>
              super.traverse(other)
          }
        }

        def enclosingValDef = enclosingDef.reverse.headOption
      }

      val f = new
                      Finder()
      val m = c.enclosingMethod
      if (m == null) {
        f.traverse(c.enclosingClass)
      }
      else
        f.traverse(m)
      f.enclosingDef.reverse
    }

    def createVDef[A: c.WeakTypeTag](op: c.Expr[_]) =
    {
      val fc = createFContext
      reify {
        val _prefix = c.prefix.splice.asInstanceOf[Frame[A]]
        val _fc = fc.splice
        val _cl = op.splice.getClass
      }
    }


    def opGen[A:c.WeakTypeTag, Out](op:c.Expr[_], args:Seq[c.Expr[_]]) = {
      //val vdef = createVDef[A](op)
      val fc = createFContext

      // This macro creates NewOp(_fc, _prefix, f)
      val e = c.Expr[Frame[Out]](
        Apply(
          Select(op.tree, TermName("apply")),
          List(
            Ident(TermName("_fc")),
            Ident(TermName("_prefix"))
          ) ++ args.map(_.tree).toList
        )
      )
      // Wrap with a block to hide the above variable definitions from the outer context
      reify {
        {
          val _prefix = c.prefix.splice.asInstanceOf[Frame[A]]
          val _fc = fc.splice
          val _cl = op.splice.getClass
          e.splice
        }
      }
    }

    def opSingleGen[A:c.WeakTypeTag, Out](op:c.Expr[_], args:Seq[c.Expr[_]]) = {
      val fc = createFContext
      // This macro creates NewOp(_fc, _prefix, f)
      val e = c.Expr[Single[Out]](
        Apply(
          Select(op.tree, TermName("apply")),
          List(
            Ident(TermName("_fc")),
            Ident(TermName("_prefix"))
          ) ++ args.map(_.tree).toList
        )
      )
      // Wrap with a block to hide the above variable definitions from the outer context
      reify {
        {
          val _prefix = c.prefix.splice.asInstanceOf[Single[A]]
          val _fc = fc.splice
          val _cl = op.splice.getClass
          e.splice
        }
      }
    }

  }

  /**
   * Generating a new InputFrame[A] from Seq[A]
   * @return
   */
  def mNewFrame[A:c.WeakTypeTag](c: Context)(in: c.Expr[Seq[A]]) = {
    import c.universe._
    q"InputFrame(${fc(c)}, $in)"
  }

  def mSQL(c:Context)(args:c.Tree*) = {
    import c.universe._
    q"RawSQL(${fc(c)}, ${c.prefix.tree}, Seq(..$args))"
  }

  def fc(c:Context) = new MacroHelper[c.type](c).createFContext

  def mAs[A:c.WeakTypeTag](c: Context) = {
    import c.universe._
    q"CastAs(${fc(c)}, ${c.prefix.tree})"
  }

  def mFilter[A:c.WeakTypeTag](c:Context)(condition:c.Tree) = {
    import c.universe._
    q"FilterOp(${fc(c)}, ${c.prefix.tree}, ${condition})"
  }

  def mSelect[A:c.WeakTypeTag](c:Context)(cols:c.Tree*) = {
    import c.universe._
    q"ProjectOp(${fc(c)}, ${c.prefix.tree}, Seq(..$cols))"
  }

  def mLimit[A:c.WeakTypeTag](c:Context)(rows:c.Tree) = {
    import c.universe._
    q"LimitOp(${fc(c)}, ${c.prefix.tree}, ${rows}, 0)"
  }

  def mLimitWithOffset[A:c.WeakTypeTag](c:Context)(rows:c.Tree, offset:c.Tree) = {
    import c.universe._
    q"LimitOp(${fc(c)}, ${c.prefix.tree}, ${rows}, ${offset})"
  }

}