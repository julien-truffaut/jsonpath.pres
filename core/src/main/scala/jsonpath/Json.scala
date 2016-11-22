package jsonpath

import monocle.function.Plated
import monocle.std.double.doubleToInt
import monocle.{Prism, Traversal}

import scalaz.Applicative
import scalaz.std.list._
import scalaz.std.map._
import scalaz.syntax.traverse._

sealed trait Json {
  def asObj: Option[Map[String, Json]] = Json.jObj.getOption(this)
  def asArray: Option[List[Json]] = Json.jArr.getOption(this)
  def asInt: Option[Int] = Json.jInt.getOption(this)

  override def toString: String = toString(1)

  def toString(depth: Int): String =
    this match {
    case JNull    => "null"
    case JBool(v) => v.toString
    case JNum(v)  => if(v.isValidInt) v.toInt.toString else v.toString
    case JStr(v)  => "\"" + v.toString + "\""
    case JArr(v)  => v.map(_.toString(depth)).mkString("[", ", ", "]")
    case JObj(vs) =>
      vs.map{ case (k, v) =>
        spaces(depth) + s""""$k" : ${v.toString(depth + 1)}"""
      }.mkString("{\n", ",\n", "\n" + spaces(depth - 1) + "}")
  }

  private def spaces(depth: Int): String = List.fill(depth)("  ").mkString
}

case object JNull extends Json
case class JBool(v: Boolean) extends Json
case class JNum(v: Double) extends Json
case class JStr(v: String) extends Json

case class JArr(v: List[Json]) extends Json
case class JObj(v: Map[String, Json]) extends Json

object Json {
  val jNull = Prism.partial[Json, Unit]{case JNull => ()}(_ => JNull)
  val jBool = Prism.partial[Json, Boolean]{case JBool(v) => v}(JBool)
  val jNum  = Prism.partial[Json, Double]{case JNum(v) => v}(JNum)
  val jStr  = Prism.partial[Json, String]{case JStr(v) => v}(JStr)
  val jArr  = Prism.partial[Json, List[Json]]{case JArr(v) => v}(JArr)
  val jObj  = Prism.partial[Json, Map[String, Json]]{case JObj(v) => v}(JObj)

  val jInt = jNum composePrism doubleToInt

  val jDescendants = new Traversal[Json, Json]{
    def modifyF[F[_]: Applicative](f: Json => F[Json])(s: Json): F[Json] =
      s match {
        case jArr(v) => v.traverseU(f).map(jArr(_))
        case jObj(v) => v.traverseU(f).map(jObj(_))
        case _       => Applicative[F].pure(s)
      }
  }

  implicit val platedJson: Plated[Json] = new Plated[Json] {
    val plate: Traversal[Json, Json] = jDescendants
  }

}