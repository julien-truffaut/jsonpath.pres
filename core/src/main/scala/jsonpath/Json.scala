package jsonpath

import monocle.Prism

sealed trait Json

case object JNull extends Json
case class JBool(v: Boolean) extends Json
case class JNumber(v: Double) extends Json
case class JString(v: String) extends Json

case class JArr(v: List[Json]) extends Json
case class JObj(v: Map[String, Json]) extends Json

object Json {
  val jNull = Prism.partial[Json, Unit]{case JNull => ()}(_ => JNull)
  val jBool = Prism.partial[Json, Boolean]{case JBool(v) => v}(JBool)
  val jNumber = Prism.partial[Json, Double]{case JNumber(v) => v}(JNumber)
  val jString = Prism.partial[Json, String]{case JString(v) => v}(JString)
  val jArr = Prism.partial[Json, List[Json]]{case JArr(v) => v}(JArr)
  val jObj = Prism.partial[Json, Map[String, Json]]{case JObj(v) => v}(JObj)
}