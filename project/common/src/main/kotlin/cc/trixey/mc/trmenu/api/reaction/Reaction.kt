package cc.trixey.mc.trmenu.api.reaction

import cc.trixey.mc.trmenu.util.getString
import cc.trixey.mc.trmenu.util.regexKeyNullable
import taboolib.library.configuration.ConfigurationSection

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Action
 *
 * @author Score2
 * @since 2022/12/29 21:01
 */

val REGEX_CONDITION = "(condition|requirement)s?".toRegex()

fun ConfigurationSection.asRequirable(k: String = "value"): Require<Any>? {
    val condition = getString(REGEX_CONDITION)
    val value = this[k] ?: return null

    return Require(k, condition, value)
}

fun Any?.asRequirable(k: String = "value"): Require<Any>? {
    return when (this) {
        is ConfigurationSection -> asRequirable(k)
        null -> null
        else -> Require(k, null, this)
    }
}

fun ConfigurationSection.getRequirable(k: String): Require<Any>? {
    return (getConfigurationSection(k) ?: return null).asRequirable(k)
}

fun ConfigurationSection.getRequirableList(k: String): List<Require<Any>> {
    return getList(k)?.mapNotNull {
        it.asRequirable(k)
    } ?: listOf()
}

fun ConfigurationSection.getStringListRequirable(k: String): List<Require<String>> {
    val origin = getList(k) ?: return listOf()

    return origin.map {
        when (it) {
            is ConfigurationSection -> {
                Require(
                    k,
                    it.getString(REGEX_CONDITION)!!,
                    it.getString(k)!!
                )
            }

            is String -> Require(k, null, it)
            else -> throw IllegalArgumentException("Unsupported this type")
        }
    }
}

fun ConfigurationSection.getStringRequirable(k: String): Require<String>? {
    val section = getConfigurationSection(k) ?: return null
    val condition = section.getString(REGEX_CONDITION)
    val value = section.getString(k) ?: return null

    return Require(k, condition, value)
}



fun ConfigurationSection.getRequirable(regex: Regex): Require<Any>? {
    val key = regexKeyNullable(regex) ?: return null
    return getRequirable(key)
}

fun ConfigurationSection.getStringListRequirable(regex: Regex): List<Require<String>> {
    val key = regexKeyNullable(regex) ?: return listOf()
    return getStringListRequirable(key)
}

fun ConfigurationSection.getStringRequirable(regex: Regex): Require<String>? {
    val key = regexKeyNullable(regex) ?: return null
    return getStringRequirable(key)
}
