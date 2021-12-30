package indigo

import kotlin.system.exitProcess

val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
val suits = listOf("♦", "♥", "♠", "♣")
val deck = mutableListOf<String>()
val myHand = mutableListOf<String>()
val computerHand = mutableListOf<String>()
val desk = mutableListOf<String>()
val humanDesk = mutableListOf<String>()
val computerDesk = mutableListOf<String>()
var playerTurn: Boolean = false
val humanScore = mutableListOf(0, 0)
val computerScore = mutableListOf(0, 0)

fun reset() {
    while (deck.isNotEmpty()) {
        deck.removeAt(0)
    }
    for (suit in suits) {
        for (rank in ranks) {
            deck.add(rank + suit)
        }
    }
    shuffle()
}

fun emptyList(liste: MutableList<String>) {
    for (i in 0 until liste.lastIndex) {
        liste.removeAt(0)
    }
}

fun shuffle() {
    deck.shuffle()
}

fun get() {
    println("Number of cards:")
    try {
        val nb = readLine()!!.toInt()
        if (nb !in 1..52) {
            println("Invalid number of cards.")
        } else if (deck.lastIndex + 1 < nb) {
            println("The remaining cards are insufficient to meet the request.")
        } else {
            val pioche = mutableListOf<String>()
            for (i in 0 until nb) {
                pioche.add(deck[0])
                deck.removeAt(0)
            }
            println(pioche.joinToString(" "))
        }
    } catch (e: Exception) {
        println("Invalid number of cards.")

    }
}

fun callForAction() {
    println("Choose an action (reset, shuffle, get, exit):")
    when (readLine()!!.toString()) {
        "reset" -> {
            reset()
            println("Card deck is reset.")
            callForAction()
        }
        "shuffle" -> {
            shuffle()
            println("Card deck is shuffled.")
            callForAction()
        }
        "get" -> {
            get()
            callForAction()
        }
        "exit" -> {
            println("Bye")
        }
        else -> {
            println("Wrong action.")
            callForAction()
        }
    }
}

fun init() {
    println("Play first?")
    val a = readLine()!!.toString()
    when (a.uppercase()) {
        "YES" -> {
            playerTurn = true
            firstDraw()
        }
        "NO" -> {
            playerTurn = false
            firstDraw()
        }
        else -> init()
    }
}

fun hand(hand: MutableList<String>) {
    for (i in 1..6) {
        hand.add(0, deck[0])
        deck.removeAt(0)
    }
}

fun firstDraw() {
    for (i in 1..4) {
        desk.add(0, deck[0])
        deck.removeAt(0)
    }
    hand(myHand)
    hand(computerHand)
    println("Initial cards on the table: " + desk.joinToString(" "))
}
fun newTurnMessage() {
    if(desk.isEmpty()) {
        println("No cards on the table")
    } else {
        println("${desk.lastIndex + 1} cards on the table, and the top card is ${desk.last()}")
    }
}

fun computeScore(score: MutableList<Int>, deck: MutableList<String>) {
    for (i in 0..desk.lastIndex) {
        score[1] += 1 // one more card for computer
        when (desk[0].substring(0, desk[0].length - 1)) { // one figure for computer
            "A", "10", "J", "Q", "K" -> score[0] += 1
            else -> 1
        }
        deck.add(desk[0])
        desk.removeAt(0)
    }
}

fun displayScore() {
    println("Score: Player ${humanScore[0]} - Computer ${computerScore[0]}")
    println("Cards: Player ${humanScore[1]} - Computer ${computerScore[1]}")
}

fun checkComputerVictory() {
    if (desk.lastIndex > 0 && (desk[desk.lastIndex].substring(0, desk[desk.lastIndex].length - 1) == desk[desk.lastIndex - 1].substring(0, desk[desk.lastIndex - 1].length - 1) || desk[desk.lastIndex][desk[desk.lastIndex].lastIndex] == desk[desk.lastIndex - 1][desk[desk.lastIndex - 1].lastIndex])) {
        println("Computer wins cards")
        playerTurn = false
        computeScore(computerScore, computerDesk)
        displayScore()
    }
}

fun checkHumanVictory() {
    if (desk.lastIndex > 0 && (desk[desk.lastIndex].substring(0, desk[desk.lastIndex].length - 1) == desk[desk.lastIndex - 1].substring(0, desk[desk.lastIndex - 1].length - 1) || desk[desk.lastIndex][desk[desk.lastIndex].lastIndex] == desk[desk.lastIndex - 1][desk[desk.lastIndex - 1].lastIndex])) {
        println("Player wins cards")
        playerTurn = true
        computeScore(humanScore, humanDesk)
        displayScore()
    }
}

fun finalScore() {
    if (humanDesk.lastIndex > computerDesk.lastIndex) {
        humanScore[0] += 3
    } else {
        computerScore[0] += 3
    }
}

fun computerDraw() {
    newTurnMessage()
    if (computerHand.isEmpty() && deck.isEmpty()) {
        if (playerTurn) {
            computeScore(humanScore, humanDesk)
        } else {
            computeScore(computerScore, computerDesk)
        }
        finalScore()
        displayScore()
        quit()
    }
    if (computerHand.isEmpty() && deck.isNotEmpty()) {
        hand(computerHand)
    }
    println(computerHand.joinToString(" "))
    chooseCard()
    println("Computer plays ${desk.last()}")
    checkComputerVictory()
    humanDraw()
}

fun automateCard(hand: MutableList<String>): String {
    val res = mutableListOf(0, 0, 0, 0)
    val res2 = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    for (i in 0..hand.lastIndex) {
        when (hand[i].last()) {
            '♦' -> {
                res[0] = res[0] + 1
                if (res[0]> 1) {
                    return hand[i]
                }
            }
            '♥' ->  {
                res[1] = res[1] + 1
                if (res[1]> 1) {
                    return hand[i]
                }
            }
            '♠' ->  {
                res[2] = res[2] + 1
                if (res[2]> 1) {
                    return hand[i]
                }
            }
            '♣' ->  {
                res[3] = res[3] + 1
                if (res[3]> 1) {
                    return hand[i]
                }
            }
        }
    }
    for (i in 0..hand.lastIndex) {
        val rank = ranks.indexOf(hand[i].substring(0, hand[i].lastIndex))
        res2[rank] = res2[rank] + 1
        if (res2[rank]> 1) {
            return hand[i]
        }
    }
    return hand[0]
}

fun chooseCard() {
    val candidatesCards = mutableListOf<String>()
    if (computerHand.lastIndex == 0) {
        desk.add(computerHand[0])
        computerHand.removeAt(0)
    } else if (desk.isEmpty()) {
        val card = automateCard(computerHand)
        desk.add(card)
        computerHand.remove(card)
    } else {
        for (i in 0..computerHand.lastIndex) {
            if (computerHand[i].substring(0, computerHand[i].length - 1) == desk.last().substring(0, desk.last().length - 1) || computerHand[i].last() == desk.last().last()) {
                candidatesCards.add(computerHand[i])
            }
        }
        if (candidatesCards.isEmpty()) {
            val card = automateCard(computerHand)
            desk.add(card)
            computerHand.remove(card)
        } else if (candidatesCards.lastIndex == 0) {
            desk.add(candidatesCards[0])
            computerHand.remove(candidatesCards[0])
        } else {
            val card = automateCard(candidatesCards)
            desk.add(card)
            computerHand.remove(card)
        }
    }
}

fun handDisplay() {
    val res = mutableListOf<String>()
    for ((index, card) in myHand.withIndex()) {
        res.add("${index + 1})$card")
    }
    println("Cards in hand: " + res.joinToString(" "))
}

fun quit() {
    println("Game over")

    exitProcess(1)
}
fun humanAction() {
    println("Choose a card to play (1-${myHand.lastIndex + 1}):")
    val action = readLine()!!
    if (action == "exit") {
        quit()
    }
    try {
        if (action.toInt() in 1..myHand.lastIndex + 1) {
            desk.add(myHand[action.toInt() - 1])
            myHand.removeAt(action.toInt() - 1)
        } else {
            humanAction()
        }
    } catch (e: Exception) {
            humanAction()
    }
}

fun humanDraw() {
    newTurnMessage()
    if (myHand.isEmpty() && deck.isEmpty()) {
        if (playerTurn) {
            computeScore(humanScore, humanDesk)
        } else {
            computeScore(computerScore, computerDesk)
        }
        finalScore()
        displayScore()
        quit()
    }
    if (myHand.isEmpty() && deck.isNotEmpty()) {
        hand(myHand)
    }
    handDisplay()
    humanAction()
    checkHumanVictory()
    computerDraw()
}

fun main() {
    println("Indigo card game")
    reset()
    init()
    if (playerTurn) {
        humanDraw()
    } else {
        computerDraw()
    }
    //callForAction()
}
