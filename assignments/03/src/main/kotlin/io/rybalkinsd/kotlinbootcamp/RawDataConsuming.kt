package io.rybalkinsd.kotlinbootcamp

import java.lang.NumberFormatException

/**
 * makes list of Profile objects from raw data
 */
fun makeProfiles(rawProfiles: List<RawProfile>): List<Profile> {
    return rawProfiles.map { rawProfile -> makeProfile(
            rawProfile.rawData.toLowerCase().split("\n").associate { makeAttribute(it) }
    ) }
}

/**
 * from map of one user's attributes makes an object of Profile class
 * if age is not a number, it turns null
 */
fun makeProfile(attributes: Map<String, String>): Profile {
    val id = IdGenerator.next()
    val profile = when (attributes["source"]) {
        "vk" -> VkProfile(id)
        "facebook" -> FacebookProfile(id)
        "linkedin" -> LinkedinProfile(id)
        else -> throw UnknownSourceException()
    }
    return profile.apply { age = try { attributes["age"]?.toInt()
    } catch (e: NumberFormatException) { null }
        firstName = attributes["firstname"]
        lastName = attributes["lastname"]
    }
}

/**
 * from raw string like "age=18," makes Pair of strings "age" to "18"
 */
fun makeAttribute(rawAttribute: String): Pair<String, String> {
    val stringWithoutComma = rawAttribute.removeSuffix(",")
    val key = stringWithoutComma.substringBefore("=")
    val value = stringWithoutComma.substringAfter("=")
    return key to value
}

object IdGenerator : Iterator<Long> {
    private var counter = 0L
    override fun hasNext(): Boolean = counter < Long.MAX_VALUE

    override fun next(): Long = if (hasNext()) counter++ else throw LongOverHeadException()
}
class UnknownSourceException : Throwable()
class LongOverHeadException : Throwable()