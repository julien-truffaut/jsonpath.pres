package jsonpath.pimpathon

import argonaut.Json._

object Example {

  val john = obj(
    "first_name" -> jString("john"),
    "last_name"  -> jString("doe"),
    "age"        -> jNumber(42),
    "siblings"   -> jArray(List(
      obj(
        "first_name" -> jString("zoe"),
        "last_name"  -> jString("doe"),
        "age"        -> jNumber(28)
      ),
      obj(
        "first_name" -> jString("dan"),
        "last_name"  -> jString("doe"),
        "age"        -> jNumber(24)
      )
    ))
  )

  import pimpathon.argonaut.JsonFrills

  john.descendant("siblings/*/age").int.modify(_ + 1)
  john.descendant("siblings/*").renameField("last_name", "family_name")

}
