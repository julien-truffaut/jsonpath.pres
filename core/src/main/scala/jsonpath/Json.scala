package jsonpath

import monocle.Prism
import monocle.std.double.doubleToInt

sealed trait Json {
  override def toString: String = toString(1)

  def toString(depth: Int): String =
    this match {
    case JNull    => "null"
    case JBool(v) => v.toString
    case JNum(v)  => v.toString
    case JStr(v)  => "\"" + v.toString + "\""
    case JArr(v)  => v.mkString("[", ", ", "]")
    case JObj(vs) =>
      vs.map{ case (k, v) =>
        List.fill(depth)("  ").mkString + s""""$k" : ${v.toString(depth + 1)}"""
      }.mkString("{\n", ",\n", "\n}")
  }
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
}