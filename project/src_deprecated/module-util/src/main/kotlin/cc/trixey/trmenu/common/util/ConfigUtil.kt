package cc.trixey.trmenu.common.util

import taboolib.library.configuration.ConfigurationSection

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.ConfigUtil
 *
 * @author Score2
 * @since 2022/12/29 21:07
 */

fun ConfigurationSection.regexKey(regex: Regex): String {
    return getKeys(false).find { regex.matches(it) }!!
}

fun ConfigurationSection.regexKeyNullable(regex: Regex): String? {
    return getKeys(false).find { regex.matches(it) }
}

operator fun ConfigurationSection.get(regex: Regex): Any? {
    return get(regexKeyNullable(regex) ?: return null)
}

fun ConfigurationSection.getString(regex: Regex): String? {
    return getString(regexKeyNullable(regex) ?: return null)
}

fun ConfigurationSection.getInt(regex: Regex): Int? {
    return getInt(regexKeyNullable(regex) ?: return null)
}

fun ConfigurationSection.getConfigurationSection(regex: Regex): ConfigurationSection? {
    return getConfigurationSection(regexKeyNullable(regex) ?: return null)
}
