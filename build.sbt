scalaVersion := "2.12.5"

val akkaVersion = "2.5.11"

libraryDependencies ++= Seq(
  "com.pubnub" % "pubnub" % "3.5.6",
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)
