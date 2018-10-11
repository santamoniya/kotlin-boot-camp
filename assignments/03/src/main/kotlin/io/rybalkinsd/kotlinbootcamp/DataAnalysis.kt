package io.rybalkinsd.kotlinbootcamp

class RawProfile(val rawData: String)

enum class DataSource {
    FACEBOOK,
    VK,
    LINKEDIN
}

sealed class Profile(
    var id: Long,
    var firstName: String? = null,
    var lastName: String? = null,
    var age: Int? = null,
    var dataSource: DataSource
)

/**
 * Task #1
 * Declare classes for all data sources
 */
class FacebookProfile(id: Long) : Profile(dataSource = DataSource.FACEBOOK, id = id)
class VkProfile(id: Long) : Profile(dataSource = DataSource.VK, id = id)
class LinkedinProfile(id: Long) : Profile(dataSource = DataSource.LINKEDIN, id = id)

/**
 * Here are Raw profiles to analyse
 */
val rawProfiles = listOf(
    RawProfile("""
            firstName=alice,
            age=16,
            source=facebook
            """.trimIndent()
    ),
    RawProfile("""
            firstName=Dent,
            lastName=kent,
            age=88,
            source=linkedin
            """.trimIndent()
    ),
    RawProfile("""
            firstName=alla,
            lastName=OloOlooLoasla,
            source=vk
            """.trimIndent()
    ),
    RawProfile("""
            firstName=bella,
            age=36,
            source=vk
            """.trimIndent()
    ),
    RawProfile("""
            firstName=angel,
            age=I will not tell you =),
            source=facebook
            """.trimIndent()
    ),

    RawProfile(
        """
            lastName=carol,
            source=vk
            age=49,
            """.trimIndent()
    ),
    RawProfile("""
            firstName=bob,
            lastName=John,
            age=47,
            source=linkedin
            """.trimIndent()
    ),
    RawProfile("""
            lastName=kent,
            firstName=dent,
            age=88,
            source=facebook
            """.trimIndent()
    )
)

val profiles: List<Profile> = makeProfiles(rawProfiles)

/**
 * Task #2
 * Find the average age for each datasource (for profiles that has age)
 */
val avgAge: Map<DataSource, Double> = profiles
        .groupBy { it.dataSource }
        .mapValues { entry -> entry.value.asSequence().filter { it.age != null }.map { it.age!!.toDouble() }.average() }

/**
 * Task #3
 * Group all user ids together with all profiles of this user.
 * We can assume users equality by : firstName & lastName & age
 */
val groupedProfiles: Map<Long, List<Profile>> = profiles.groupBy { listOf(it.age, it.lastName, it.firstName) }
        .values.associateBy { IdGenerator.next() }