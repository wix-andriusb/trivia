package com.adaptionsoft.games.uglytrivia

import java.util.{LinkedList, ArrayList}

sealed trait Category
object Category {
  case object Pop extends Category
  case object Science extends Category
  case object Sports extends Category
  case object Rock extends Category

  val All = Set(Pop, Science, Sports, Rock)
}

class Game(playerNames: Seq[String]) {
  var players: ArrayList[String] = new ArrayList[String]
  var places: Array[Int] = new Array[Int](6)
  var purses: Array[Int] = new Array[Int](6)
  var inPenaltyBox: Array[Boolean] = new Array[Boolean](6)

  val questions: Map[Category, LinkedList[String]] =
    Category.All.map(category => category -> new LinkedList[String]).toMap

  var currentPlayer: Int = 0
  var isGettingOutOfPenaltyBox: Boolean = false

  def initialize() {
    var i: Int = 0
    while (i < 50) {
      questions(Category.Pop).addLast("Pop Question " + i)
      questions(Category.Science).addLast(("Science Question " + i))
      questions(Category.Sports).addLast(("Sports Question " + i))
      questions(Category.Rock).addLast("Rock Question " + i)
      i += 1
    }
  }

  initialize()

  playerNames.foreach { playerName =>
    add(playerName)
  }

  def println(o: Any): Unit = {
    System.out.println(s"$o")
  }

  def isPlayable: Boolean = (howManyPlayers >= 2)

  private def add(playerName: String): Boolean = {
    players.add(playerName)
    places(howManyPlayers) = 0
    purses(howManyPlayers) = 0
    inPenaltyBox(howManyPlayers) = false
    println(playerName + " was added")
    println("They are player number " + players.size)
    true
  }

  def howManyPlayers: Int = players.size

  def roll(roll: Int): Unit = {
    println(players.get(currentPlayer) + " is the current player")
    println("They have rolled a " + roll)
    if (inPenaltyBox(currentPlayer)) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true
        println(players.get(currentPlayer) + " is getting out of the penalty box")
        places(currentPlayer) = places(currentPlayer) + roll
        if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
        println(players.get(currentPlayer) + "'s new location is " + places(currentPlayer))
        println("The category is " + currentCategory)
        askQuestion
      }
      else {
        println(players.get(currentPlayer) + " is not getting out of the penalty box")
        isGettingOutOfPenaltyBox = false
      }
    }
    else {
      places(currentPlayer) = places(currentPlayer) + roll
      if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
      println(players.get(currentPlayer) + "'s new location is " + places(currentPlayer))
      println("The category is " + currentCategory)
      askQuestion
    }
  }

  private def askQuestion: Unit = {
    val question = questions(currentCategory).removeFirst
    println(question)
  }

  private def currentCategory: Category = {
    places(currentPlayer) match {
      case 0 | 4 |  8 => Category.Pop
      case 1 | 5 |  9 => Category.Science
      case 2 | 6 | 10 => Category.Sports
      case _          => Category.Rock
    }
  }

  def wasCorrectlyAnswered: Boolean = {
    if (inPenaltyBox(currentPlayer)) {
      if (isGettingOutOfPenaltyBox) {
        println("Answer was correct!!!!")
        purses(currentPlayer) += 1
        println(players.get(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
        var winner: Boolean = didPlayerWin
        currentPlayer += 1
        if (currentPlayer == players.size) currentPlayer = 0
        winner
      }
      else {
        currentPlayer += 1
        if (currentPlayer == players.size) currentPlayer = 0
        true
      }
    }
    else {
      println("Answer was corrent!!!!")
      purses(currentPlayer) += 1
      println(players.get(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
      var winner: Boolean = didPlayerWin
      currentPlayer += 1
      if (currentPlayer == players.size) currentPlayer = 0
      winner
    }
  }

  def wrongAnswer: Boolean = {
    println("Question was incorrectly answered")
    println(players.get(currentPlayer) + " was sent to the penalty box")
    inPenaltyBox(currentPlayer) = true
    currentPlayer += 1
    if (currentPlayer == players.size) currentPlayer = 0
    true
  }

  private def didPlayerWin: Boolean = !(purses(currentPlayer) == 6)
}