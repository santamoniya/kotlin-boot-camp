package io.matveyeva.kotlinbootcamp.bullsandcows

import java.io.FileNotFoundException
import java.lang.NullPointerException
import java.util.Random

object BullsAndCowsApplication {
    private const val welcomeText = "Welcome to \"Bulls and Cows\" game!"
    private val wordList = getWordsList()

    fun execute() {
        println(welcomeText)
        while (true) {
            val hiddenWord = extractWordFromDictionary()
            println("I offered a ${hiddenWord.length}-letter word, your guess?")
            Game(hiddenWord).start()
            var answer: String
            do {
                println("Wanna play again? Y/N")
                answer = readLine()?.toLowerCase() ?: throw NullPointerException()
            } while (answer != "n" && answer != "y")
            if (answer == "n") return
            if (answer == "y") continue
        }
    }

    private fun getWordsList(): List<String> = try {
        BullsAndCowsApplication::class.java.getResource("/dictionary.txt").readText().split("\n")
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        emptyList()
    }

    private fun extractWordFromDictionary(): String {
        val indexOfWord = Random().nextInt(wordList.size)
        return wordList[indexOfWord]
    }
}