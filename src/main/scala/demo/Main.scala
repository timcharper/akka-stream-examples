package demo

import scala.concurrent.Future
import scala.util.Success
import Twitter.tweetFormat
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{QueueOfferResult, ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl._
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import play.api.libs.json._

object Main extends App {
  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()
  import as.dispatcher

  val tweetSource: Source[Tweet, () => Unit] =
    Source.queue[Tweet](5, OverflowStrategy.dropNew).
      mapMaterializedValue { input =>
        println("Start")
        Twitter.subscribePubnubChannel("pubnub-twitter") { tweet =>
          input.offer(tweet).onComplete {
            case Success(QueueOfferResult.Enqueued) =>
              ()
            case _ =>
              println(s"Bummer! ${tweet} dropped")
          }
        }

        { () => input.complete() }
      }

  val summarizeOnBackpressure = Flow[Tweet].
    conflateWithSeed(tweet => Map(tweet.place.country_code.getOrElse("??") -> 1)) { (m, tweet) =>
      val country = tweet.place.country_code.getOrElse("??")
      m + (country -> (m.getOrElse(country, 0) + 1))
    }


  val sockets: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("0.0.0.0", 8888)

  val pipeline = tweetSource.
    via(summarizeOnBackpressure).
    zip(sockets)

  pipeline.runForeach { case (tweet, socket) =>
    socket.handleWith(Helpers.writing(Json.toJson(tweet).toString + "\n"))
  }

  // nc localhost 8888 | jq .
}

