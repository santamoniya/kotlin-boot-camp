package io.rybalkinsd.kotlinbootcamp.practice


/**
 * NATO phonetic alphabet
 */
val alphabet = setOf("Alfa", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliett", "Kilo", "Lima", "Mike", "November", "Oscar", "Papa", "Quebec", "Romeo", "Sierra", "Tango", "Uniform", "Victor", "Whiskey", "Xray", "Yankee", "Zulu")

/**
 * A mapping for english characters to phonetic alphabet.
 * [ a -> Alfa, b -> Bravo, ...]
 */
val association: Map<Char, String> = alphabet.associateBy{ it[0].toLowerCase() }

/**
 * Extension function for String which encode it according to `association` mapping
 *
 * @return encoded string
 *
 * Example:
 * "abc".encode() == "AlfaBravoCharlie"
 *
 */
fun String.encode(): String  = map { it.toLowerCase() }.map{ association[it] ?: it }.joinToString("")

/**
 * A reversed mapping for association
 * [ alpha -> a, bravo -> b, ...]
 */
val reversedAssociation: Map<String, Char> = alphabet.associate { it to it[0] }


/**
 * Extension function for String which decode it according to `reversedAssociation` mapping
 *
 * @return encoded string or null if it is impossible to decode
 *
 * Example:
 * "alphabravocharlie".decode() == "abc"
 * "charliee".decode() == null
 *
 */
fun String.decode(): String? {
    var str = this.toLowerCase()
    var output = ""
    while( str != ""){
        val pattern = association[str[0]]
        if(pattern != null){
            if (pattern.length <= str.length) {
                if (str.substring(0, pattern.length) == pattern.toLowerCase()){
                    output += str[0]
                    str = str.substring(pattern.length)
                }
                else return null
            }
            else return null
        }
        else {
            output += str[0]
            str = str.substring(1)
        }
    }
    return output
}