package jsonpath

import jsonpath.Json._
import monocle.{Optional, Traversal}
import monocle.function.all
import monocle.std.list._
import monocle.std.map._

import scala.language.dynamics

case class JsonPath(json: Optional[Json, Json]) extends Dynamic {
  def `null` : Optional[Json, Unit]       = json composePrism jNull
  def boolean: Optional[Json, Boolean]    = json composePrism jBool
  def int    : Optional[Json, Int]        = json composePrism jInt
  def double : Optional[Json, Double]     = json composePrism jNum
  def string : Optional[Json, String]     = json composePrism jStr
  def arr    : Optional[Json, List[Json]] = json composePrism jArr
  def obj    : Optional[Json, Map[String, Json]] = json composePrism jObj

  def index(i: Int)     : JsonPath = JsonPath(arr composeOptional all.index(i))
  def index(key: String): JsonPath = JsonPath(obj composeOptional all.index(key))

  def selectDynamic(field: String): JsonPath = JsonPath(obj composeOptional all.index(field))

  def each: JsonTraversalPath =
    JsonTraversalPath(json composeTraversal jDescendants)
}

object JsonPath {
  val root = JsonPath(Optional.id)
}

case class JsonTraversalPath(json: Traversal[Json, Json]) extends Dynamic {
  def `null` : Traversal[Json, Unit]       = json composePrism jNull
  def boolean: Traversal[Json, Boolean]    = json composePrism jBool
  def int    : Traversal[Json, Int]        = json composePrism jInt
  def double : Traversal[Json, Double]     = json composePrism jNum
  def string : Traversal[Json, String]     = json composePrism jStr
  def arr    : Traversal[Json, List[Json]] = json composePrism jArr
  def obj    : Traversal[Json, Map[String, Json]] = json composePrism jObj

  def index(i: Int)     : JsonTraversalPath = JsonTraversalPath(arr composeOptional all.index(i))
  def index(key: String): JsonTraversalPath = JsonTraversalPath(obj composeOptional all.index(key))

  def selectDynamic(field: String): JsonTraversalPath = JsonTraversalPath(obj composeOptional all.index(field))

  def each: JsonTraversalPath =
    JsonTraversalPath(json composeTraversal jDescendants)
}

object JsonTraversalPath {
  val root = JsonTraversalPath(Traversal.id)
}