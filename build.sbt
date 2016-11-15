lazy val baseSettings: Seq[Setting[_]] = Seq(
  scalaVersion       := "2.12.0",
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
    "com.github.julien-truffaut"  %% "monocle-core"     % "1.3.2",
    "com.github.julien-truffaut"  %% "monocle-macro"    % "1.3.2",
    "io.argonaut"                 %% "argonaut-monocle" % "6.2-RC1",
    "io.gatling"                  %% "jsonpath"         % "0.6.8",
    "com.github.stacycurl"        %% "pimpathon"        % "1.8.0"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "Stacy Curl's repo" at "http://dl.bintray.com/stacycurl/repo/"
  )
)

lazy val jsonpath = project.in(file("."))
  .settings(moduleName := "jsonpath")
  .settings(baseSettings: _*)
  .aggregate(core, slides)
  .dependsOn(core, slides)

lazy val core = project
  .settings(moduleName := "jsonpath-core")
  .settings(baseSettings: _*)

lazy val slides = project
  .settings(moduleName := "jsonpath-slides")
  .settings(baseSettings: _*)
  .settings(tutSettings: _*)
  .settings(
    tutSourceDirectory := baseDirectory.value / "tut",
    tutTargetDirectory := baseDirectory.value / "tut-out"
  ).dependsOn(core)