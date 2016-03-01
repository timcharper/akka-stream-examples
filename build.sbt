scalaVersion := "2.11.7"

val akkaVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.pubnub" % "pubnub" % "3.5.6",
  "com.typesafe.play" %% "play-json" % "2.4.3",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)
