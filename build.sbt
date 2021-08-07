val scala3Version = "3.0.1"

val circeV = "0.14.1"
val zioV   = "1.0.10"
val sttpV  = "3.3.12"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "dev.zio" %% "izumi-reflect" % "1.1.3",
      "dev.zio" %% "zio-config"          % "1.0.6",
      "dev.zio" %% "zio-config-typesafe" % "1.0.6",
      "com.softwaremill.sttp.client3" %% "core"                          % sttpV,
      "com.softwaremill.sttp.client3" %% "circe"                         % sttpV,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % sttpV,
      "io.circe"                      %% "circe-generic"                 % circeV,
      "dev.zio"                       %% "zio"                           % zioV
    )
  )
