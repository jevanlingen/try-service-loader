package io.jacob

import kotlin.math.max

class PlayerTwoContest : Contest {
    override fun highestNumber(a: Int, b: Int) =
        max(a.toDouble(), b.toDouble()).toInt()

    override fun removeDuplicates(s: String): String {
        val seen = mutableSetOf<Char>()
        return buildString {
            for (char in s) {
                if (char !in seen) {
                    seen.add(char)
                    append(char)
                }
            }
        }
    }
}
