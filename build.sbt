lazy val root = project.in(file("."))
  .settings(
    name                           := "KDTree",
    version                        := "0.1.0-SNAPSHOT",
    scalaVersion                   := "2.12.6",
    scalacOptions                 ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xfuture", "-Xlint:-stars-align,_"),
    crossPaths                     := false,
    javacOptions in Compile       ++= Seq("-target", "1.8", "-source", "1.8"),
    javacOptions in (Compile, doc) := Nil,
    autoScalaLibrary               := false,
    libraryDependencies ++= Seq(
      "com.novocode"      % "junit-interface"   % "0.11"  % Test,
      "org.junit.jupiter" % "junit-jupiter-api" % "5.2.0" % Test
    )
  )
