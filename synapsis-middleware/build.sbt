name := "synapsis-middleware"
organization := "luca.pascucci"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

// SPECIFICA LA VERSIONE DI JAVA DA UTILIZZARE
initialize := {
  val _ = initialize.value // run the previous initialization
  val required = "12"
  val current  = sys.props("java.specification.version")
  assert(current == required, "Unsupported JDK: java.specification.version $current != $required")
}

libraryDependencies += guice

// slf4j
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j"  % "2.5.25"
)

// At the core of Akka: A model for concurrency and distribution without all the pain of threading primitives.
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.25",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.25" % Test
)

// An intuitive and safe way to do asynchronous, non-blocking backpressured stream processing.
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.25",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.25" % Test
)

// Modern, fast, asynchronous, streaming-first HTTP server and client.
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.9",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.9" % Test
)

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"
