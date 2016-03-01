package demo

import Twitter.tweetFormat
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl._
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import play.api.libs.json._
import scala.concurrent.Future

object Main extends App {
  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()
  import as.dispatcher

  val tweetSource: Source[Tweet, () => Unit] =
    Source.queue[Tweet](5, OverflowStrategy.dropNew).
      mapMaterializedValue { input =>
        println("Start")
        Twitter.subscribePubnubChannel("pubnub-twitter") { tweet =>
          input.offer(tweet)
        }

        { () => input.complete() }
      }

  val sockets: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("0.0.0.0", 8888)


  tweetSource.zip(sockets).runForeach { case (tweet, socket) =>
    socket.handleWith(Helpers.writing(Json.toJson(tweet).toString + "\n"))
  }

  // nc localhost 8888 | jq .
}

