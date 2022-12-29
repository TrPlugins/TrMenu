package cc.trixey.mc.trmenu.api.action

import taboolib.library.configuration.ConfigurationSection
import cc.trixey.mc.trmenu.util.getString
import cc.trixey.mc.trmenu.util.regexKeyNullable

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Action
 *
 * @author Score2
 * @since 2022/12/29 21:01
 */
val REGEX_CONDITION = "(condition|requirement)s?".toRegex()

fun ConfigurationSection.getStringListRequirable(k: String, deep: Boolean = true): Require<List<String>?> {
    val condition = getString(REGEX_CONDITION)
    val list = getList(k) ?: return Require(condition, null)
    val content = if (deep) {
        list.map {
            when {
                it is ConfigurationSection -> {

                }
            }


        }
    } else {
        list.map { it.toString() }
    }

    return Require(condition, content)
}

fun ConfigurationSection.getStringRequirable(k: String): Require<String?> {
    val condition = getString(REGEX_CONDITION)
    val content = getString(k)

    return Require(condition, content)
}


fun ConfigurationSection.getStringListRequirable(regex: Regex, deep: Boolean = true): Require<List<String>>? {
    val key = regexKeyNullable(regex) ?: return null
    return getStringListRequirable(key, deep)
}

fun ConfigurationSection.getStringRequirable(regex: Regex): Require<String?>? {
    val key = regexKeyNullable(regex) ?: return null
    return getStringRequirable(key)
}