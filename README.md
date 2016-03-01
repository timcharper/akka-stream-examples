# Akka Streams examples

This is a series of examples I used in my presentation on Akka Streams. To step
through them, start with this command:

    git checkout master~7

Then, to step through, decrement the `7` until you reach `0`. All of the action
happens in `src/main/scala/demo/Main.scala`

## Running

You need `sbt` installed, and, naturally, the JDK, version 8.

Once installed, run sbt:

    $ sbt

This will get you in to the shell. To run a program:

    > re-start

This will download dependencies, compile and run the Main object in the
background (the one in `src/main/scala/demo/Main.scala` and extends `App`).

The sbt terminal is still active in the foreground and responds to input. To
stop the program, type `re-stop` and press enter. Yes, you'll have to do this
blindly if the twitter examples are fludding your terminal with lots of text.

It's not necessary to restart `sbt` when you change the program or step through
the examples with `git checkout`. Simply type `re-start` again, and it will
recompile, stop the program (if running), and then start it anew.

