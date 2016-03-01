package demo

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{OverflowStrategy,ActorMaterializer}
import akka.stream.scaladsl._

object Main extends App {
  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()
  import as.dispatcher

  val tweetSource: Source[Tweet, () => Unit] =
    Source.queue[Tweet](5, OverflowStrategy.dropNew).
      mapMaterializedValue { input =>
        // Does not print until code is run
        println("Start")
        Twitter.subscribePubnubChannel("pubnub-twitter") { tweet =>
          input.offer(tweet)
        }

        { () => input.complete() }
      }

  // tweetSource.runForeach(println)
}

