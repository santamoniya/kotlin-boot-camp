package io.rybalkinsd.kotlinbootcamp

import junit.framework.TestCase.assertTrue
import org.junit.Test

class DataAnalysisTest {

    @Test
    fun `check avg age`() {
        assertTrue(avgAge.isNotEmpty())
    }

    @Test
    fun `check grouped profiles`() {
        assertTrue(groupedProfiles.isNotEmpty())
    }

    @Test
    fun `check if grouped profiles contains all users`() {
        assertTrue(groupedProfiles.values.flatten().size == rawProfiles.size)
    }

    @Test
    fun `check null age in profile`() {
        val rawProfiles = listOf(RawProfile(
                """
            lastName=carol,
            source=vk
            age=lol,
            """.trimIndent()
        ))
        assertTrue(makeProfiles(rawProfiles)[0].age == null)
    }
}