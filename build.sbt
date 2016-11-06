lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion       := "2.11.8",
  scalacOptions     ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions", "-language:existentials",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  ),
  libraryDependencies ++= Seq(
    "com.github.julien-truffaut"  %%  "monocle-core"    % monocleVersion,
    "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleVersion
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.bintrayRepo("stanch", "maven"),
    Resolver.bintrayRepo("drdozer", "maven")
  )
)

lazy val circeVersion = "0.5.4"
lazy val monocleVersion = "1.3.1"

lazy val jsonpath = project.in(file("."))
  .settings(moduleName := "jsonpath")
  .settings(baseSettings: _*)
  .aggregate(core, slides)
  .dependsOn(core, slides)

lazy val core = project
  .settings(moduleName := "jsonpath-core")
  .settings(libraryDependencies ++= Seq(
    "org.stanch" %% "reftree"       % "0.7.2",
    "io.circe"   %% "circe-core"    % circeVersion
  ))
  .settings(baseSettings: _*)


lazy val slides = project
  .settings(moduleName := "jsonpath-slides")
  .settings(baseSettings: _*)
  .settings(tutSettings: _*)
  .settings(
    tutSourceDirectory := baseDirectory.value / "tut",
    tutTargetDirectory := baseDirectory.value / "tut-out"
  ).dependsOn(core)