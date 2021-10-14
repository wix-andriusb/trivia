package com.adaptionsoft.games.uglytrivia

import scala.io.Source
import java.io.{PrintStream, ByteArrayOutputStream}

import org.scalatest._
import flatspec._
import matchers._

import com.adaptionsoft.games.uglytrivia.Game
import java.util.Random


class GameTest extends AnyFlatSpec with should.Matchers {
  def game(): Unit = {
    var notAWinner = false

    var aGame = new Game(Seq("Chet", "Pat", "Sue"))

    var rand: Random = new Random(0)

    do {
      aGame.roll(rand.nextInt(5) + 1)
      if (rand.nextInt(9) == 7) {
        notAWinner = aGame.wrongAnswer
      }
      else {
        notAWinner = aGame.wasCorrectlyAnswered
      }
    } while (notAWinner)
  }

  def run(seed: Int): String = {
    val outStream = new ByteArrayOutputStream()
    val out = new PrintStream(outStream)
    System.setOut(out)
    game()
    outStream.toString.trim
  }

  "Golden record" should "be the same" in {
    val expected = Source.fromResource("out.txt").getLines.toSeq.mkString("\n")

    run(0) should be (expected)
  }

  "Game" should "be not playable with 1 or no player" in {
    new Game(Seq.empty).isPlayable should be (false)
    new Game(Seq("a")).isPlayable should be (false)
  }

  "Game" should "be not playable with 2 players and more" in {
    new Game(Seq("a", "b")).isPlayable should be (true)
    new Game(Seq("a", "b", "c")).isPlayable should be (true)
  }
}
