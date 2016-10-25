package jsonpath.diagram

import java.nio.file.Paths

import io.circe.Json
import reftree.contrib.SimplifiedInstances._
import reftree.{Diagram, RefTree, RefTreeSyntax, ToRefTree}

object Test extends App {

  implicit def refTreeJson: ToRefTree[Json] = new ToRefTree[Json] {
    def refTree(value: Json): RefTree = value.fold(
      RefTree.Null(),
      _.refTree,
      _.toDouble.refTree,
      _.refTree,
      _.refTree,
      _.toMap.refTree
    )
  }

  val diagram = Diagram(
    defaultOptions = Diagram.Options(density = 100),
    defaultDirectory = Paths.get("images")
  )

  diagram.render("string")(Json.fromString("Hello World"))
  diagram.render("boolean")(Json.False)
  diagram.render("int")(Json.fromInt(12))
  diagram.render("double")(Json.fromDouble(123.567))
  diagram.render("list")(Json.arr(Json.False, Json.fromString("Yo")))

  val john = Json.obj(
    "first-name" -> Json.fromString("John"),
    "last-name"  -> Json.fromString("Doe"),
    "age"        -> Json.fromInt(25)
  )

  diagram.render("object")(john)
}
