package eu.slickbot.arso.extension

import java.util.regex.Matcher

fun String.replacePrefix(oldPrefix: String, newPrefix: String): String {
    return if (startsWith(oldPrefix)) {
        "$newPrefix${removePrefix(oldPrefix)}"
    } else {
        this
    }
}

fun String.removePrefixSuffix(prefix: CharSequence, suffix: CharSequence): String {
    return removePrefix(prefix).removeSuffix(suffix)
}

fun String.removePrefixSuffix(both: CharSequence): String {
    return removePrefix(both).removeSuffix(both)
}

fun CharSequence.matcher(input: Regex): Matcher {
    return input.toPattern().matcher(this)
}

fun CharSequence.indicesOf(input: String): List<Int> {
    val list = mutableListOf<Int>()
    var currentIndex = 0

    while (true) {
        val startIdx = indexOf(input, startIndex = currentIndex)
        if (startIdx == -1)
            break
        val endIdx = startIdx + input.length

        list += startIdx
        currentIndex = endIdx + 1

        if (currentIndex >= length) {
            break
        }
    }

    return list
}
