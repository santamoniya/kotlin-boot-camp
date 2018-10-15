package io.matveyeva.kotlinbootcamp.bullsandcows

import junit.framework.TestCase.assertEquals
import org.junit.Test
class Test {
    @Test
    fun `same letters in word`() {
        val result = Game("cetes").checkWord("katee")
        assertEquals(Game.Result(2, 1), result)
    }
}