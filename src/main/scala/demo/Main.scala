package demo

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

object Main extends App {

  implicit val as = ActorSystem("hi")
  implicit val materializer = ActorMaterializer()

  /*
   There are three primary primitive shapes in Akka Streams

   - Source
   - Flow
   - Sink
   */
  /* Sources have a closed head, but open tail

   +--------
   | SOURCE
   +--------

   */
  val numbers = Source(List(1,2,3))


  /*
   Flows have open ends

   ------
    FLOW
   ------

   */
  val increment = Flow[Int].map(_ + 1)

  /* Sinks are the opposite of sources

   ------+
    SINK |
   ------+
   */
  val sink = Sink.foreach(println)


  /*
   +--------   ------    +-----------------------------
   | SOURCE  +  FLOW   = | SOURCE <with flow behavior>
   +--------   ------    +-----------------------------
   */
  val incrementedSource: Source[Int, NotUsed] =
    numbers.via(increment)

  /*
   ------   ------+    ----------------------+
    FLOW  +  SINK |  =  <flow behavior> SINK |
   ------   ------+    ----------------------+
   */
  val incrementingSink: Sink[Int, NotUsed] =
    increment.to(sink)

  /* Combine the parts together to close the graph
   +--------   ------   ------+   +---------------+
   | SOURCE  +  FLOW  +  SINK | = | RunnableGraph |
   +--------   ------   ------+   +---------------+
   */
  val graph: RunnableGraph[NotUsed] =
    numbers.via(increment).to(sink)

  // Nothing has happened yet! Once the graph is closed, lets run it
  graph.run()

  /* More shapes exist to help build complete graph topology

   - Fan out
   - Fan in
   - Routers
   - Bidirectional flows (it's like a "higher-kinded" flow)

      BIDI    +    Flow   =    Flow
   ==========   =========   ============
    <--out--     --out-\
                       |     in --> out
     --in-->     --in--/
   ==========   =========   ============

   */
}

