val scala3Version = "3.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "com.github.dapperware" %% "zio-slack-api-realtime" % "0.9.6-16-79debd5-dirty-SNAPSHOT",
      "com.github.dapperware" %% "zio-slack-api-web" % "0.9.6-16-79debd5-dirty-SNAPSHOT",
      "dev.zio" %% "zio-config"          % "1.0.6",
      "dev.zio" %% "zio-config-typesafe" % "1.0.6"
    )
  )
