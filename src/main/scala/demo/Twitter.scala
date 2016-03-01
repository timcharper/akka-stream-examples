package demo

import akka.NotUsed
import akka.stream.FlowShape
import com.pubnub.api.Callback
import com.pubnub.api.Pubnub
import play.api.libs.json._
import akka.util.ByteString
import akka.stream.scaladsl._

case class Place(country_code: Option[String])
case class Tweet(text: String, place: Place)

object Twitter {
  implicit val placeFormat = Json.format[Place]
  implicit val tweetFormat = Json.format[Tweet]

  def subscribePubnubChannel(channel: String)(handler: Tweet => Unit): Unit = {
    val pubnub = new Pubnub("", "sub-c-78806dd4-42a6-11e4-aed8-02ee2ddab7fe")
    pubnub.subscribe(
      channel,
      new Callback {
        override def successCallback(channel: String, message: Object): Unit = {
          handler(Json.parse(message.toString).as[Tweet])
        }
      }
    )
  }
}

object Helpers {
  def writing(str: String): Flow[ByteString, ByteString, NotUsed] = {
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      val ignore = b.add(Sink.ignore)
      val output = b.add(Source.single(ByteString(str)))
      FlowShape(ignore.in, output.out)
    })
  }
}

