organization in ThisBuild := "com.iheart"

val gitHubSettings = Seq(
    githubOwner := "paperculture",
    githubRepository := "play-swagger",
    githubTokenSource := TokenSource.GitConfig("github.token"),
    publishArtifact in Test := false,
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false },
    pomExtra :=
      <url>https://github.com/iheartradio/play-swagger</url>
      <scm>
        <connection>scm:git:github.com/iheartradio/play-swagger.git</connection>
        <developerConnection>scm:git:git@github.com:iheartradio/play-swagger.git</developerConnection>
        <url>github.com/iheartradio/play-swagger</url>
      </scm>
      <developers>
        <developer>
          <id>iheartradio</id>
          <name>iHeart Radio</name>
          <url>http://www.iheartradio.com/</url>
        </developer>
       </developers>,
     publishTo := githubPublishTo.value,
     publishConfiguration := publishConfiguration.value.withOverwrite(true),
     publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
 )

lazy val noPublishSettings = Seq(
  skip in publish := true,
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val scalaV = "2.12.12"

lazy val root = project.in(file("."))
  .aggregate(playSwagger, sbtPlaySwagger)
  .settings(
    gitHubSettings,
    sourcesInBase := false,
    noPublishSettings,

    scalaVersion := scalaV
  )

lazy val playSwagger = project.in(file("core"))
  .settings(
    gitHubSettings,
    Format.settings,
    Testing.settings,
    name := "play-swagger",
    libraryDependencies ++= Dependencies.playTest ++
      Dependencies.playRoutesCompiler ++
      Dependencies.playJson ++
      Dependencies.enumeratum ++
      Dependencies.test ++
      Dependencies.yaml,
    scalaVersion := scalaV,
    crossScalaVersions := Seq(scalaVersion.value, "2.13.3"),
  )


lazy val sbtPlaySwagger = project.in(file("sbtPlugin"))
  .settings(
    gitHubSettings,
    Format.settings,
    addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.17" % Provided),
    addSbtPlugin("com.typesafe.sbt" %% "sbt-web" % "1.4.3" % Provided))
  .enablePlugins(BuildInfoPlugin, SbtPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "com.iheart.playSwagger",
    name := "sbt-play-swagger",
    description := "sbt plugin for play swagger spec generation",
    sbtPlugin := true,
    scalaVersion := scalaV,
    scripted := scripted.dependsOn(publishLocal in playSwagger).evaluated,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )
