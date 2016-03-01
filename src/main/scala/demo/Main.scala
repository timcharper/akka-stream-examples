package demo

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object Main extends App {

  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()

  println("Starting\n")
  Source(List(1,2,3)).
    map { n =>
      println(s"${n}     ")
      Thread.sleep(500)
      println(s" ${n}    ")
      n
    }.
    // async.
    map { n =>
      println(s"  ${n}   ")
      Thread.sleep(500)
      println(s"   ${n}  ")
      n
    }.
    runForeach { n =>
      println(s"     ${n}!")
    }





}

