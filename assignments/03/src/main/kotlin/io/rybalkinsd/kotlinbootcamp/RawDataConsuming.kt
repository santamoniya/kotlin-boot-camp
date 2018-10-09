package io.rybalkinsd.kotlinbootcamp

import java.lang.NumberFormatException

val idGenerator = IdGenerator()

fun makeProfiles(rawProfiles: List<RawProfile>): List<Profile> {
    return rawProfiles.map { rawProfile -> makeProfile(
            rawProfile.rawData.toLowerCase().split("\n").associate { makeAttribute(it) }
    ) }
}

fun makeAttribute(rawAttribute: String): Pair<String, String> {
    val stringWithoutComma = rawAttribute.removeSuffix(",")
    val key = stringWithoutComma.substringBefore("=")
    val value = stringWithoutComma.substringAfter("=")
    return key to value
}

fun makeProfile (attributes: Map<String, String>): Profile {
    val id = idGenerator.next()
    var profile = when(attributes["source"]) {
        "vk" -> VkProfile(id)
        "facebook" -> FacebookProfile(id)
        "linkedin" -> LinkedinProfile(id)
        else -> throw UnknownSourceException()
    }
    return profile.apply { age = try { attributes["age"]?.toInt()
                                } catch (e : NumberFormatException) { null }
                    firstName = attributes["firsttname"]
                    lastName = attributes["lastname"]
    }
}
class IdGenerator : Iterator<Long> {
    var counter = 0L
    override fun hasNext(): Boolean = if (counter < Long.MAX_VALUE) true else throw LongOverHeadException()

    override fun next(): Long = counter++
}
class UnknownSourceException : Throwable()
class LongOverHeadException : Throwable()