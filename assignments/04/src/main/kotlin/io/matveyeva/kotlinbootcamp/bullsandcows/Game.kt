package io.matveyeva.kotlinbootcamp.bullsandcows

class Game(private val hiddenWord: String) {
    var attempts = 1
    var isWon = false

    fun start() {
        while (attempts <= 10 && !isWon) {
            print("$attempts attempt > ")
            val inputWord = readLine() ?: ""
            if (inputWord.length != hiddenWord.length) {
                println("Length of your word is not equals ${hiddenWord.length}")
                continue
            }
            attempts++
            val (bulls, cows) = checkWord(inputWord.toLowerCase())
            if (bulls == hiddenWord.length) isWon = true
            println("Bulls: $bulls")
            println("Cows: $cows")
        }

        if (attempts > 10) println("Your 10 attempts are spent...:( The hidden word is \"$hiddenWord\"")
        if (isWon) println("You win!")
    }

    fun checkWord(inputWord: String): Result {
        var bulls = 0
        var cows = 0
        var droppedInputWord = inputWord
        val droppedWord = hiddenWord.filterIndexed { index, c ->
            if (c == inputWord[index]) {
                bulls++
                droppedInputWord = droppedInputWord.replaceFirst(c.toString(), "")
                false
            } else true
        }

        droppedWord.forEach { if (droppedInputWord.contains(it)) { cows++
            droppedInputWord = droppedInputWord.replaceFirst(it.toString(), "") } }

        return Result(bulls, cows)
    }

    data class Result(val bulls: Int, val cows: Int)
}