package demo

object Main extends App {
  // Simple data source
  Twitter.subscribePubnubChannel("pubnub-twitter") { t =>
    println(t)
  }
}

