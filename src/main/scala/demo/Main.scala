package demo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import scala.concurrent.Future

object Main extends App {
  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()
  import as.dispatcher

  val sockets: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("0.0.0.0", 8888)

  sockets.runForeach { socket =>
    socket.handleWith(Helpers.writing("hello!\n"))
  }

  // nc localhost 8888
}

