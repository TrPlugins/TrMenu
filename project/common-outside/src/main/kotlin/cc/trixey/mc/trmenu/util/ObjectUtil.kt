package cc.trixey.mc.trmenu.util

import cc.trixey.mc.trmenu.util.regex.Regexs.REGEX_PLACEHOLDER
import cc.trixey.mc.trmenu.util.regex.Regexs.REGEX_TRUE

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.ObjectUtil
 *
 * @author Score2
 * @since 2022/12/31 11:53
 */

fun String.hasPlaceholder() =
    REGEX_PLACEHOLDER.find(this) != null

fun String.removeBlank() =
    replace("\\s*".toRegex(), "")

fun Any?.parseBoolean(): Boolean {
    return when (this) {
        is Boolean -> this
        is Int -> this == 1
        is String -> this.lowercase().matches(REGEX_TRUE)
        null -> false
        else -> this.toString().lowercase().matches(REGEX_TRUE)
    }
}