package me.arasple.mc.trmenu.util

/**
 * @author Arasple
 * @date 2021/1/24 16:57
 */
object Regexs {

    val PLACEHOLDER_API = "[%{](.+?)[%}]".toRegex()
    val ICON_KEY = "`(\\w+)`".toRegex()
    val SENTENCE = "`(.+?)`".toRegex()
    val STRING = "\\{(\\w+)}".toRegex()

    private val TRUE = "true|yes|on".toRegex()
    val FALSE = "false|no|off".toRegex()
    val BOOLEAN = "true|yes|on|false|no|off".toRegex()

    fun containsPlaceholder(string: String): Boolean {
        return PLACEHOLDER_API.find(string) != null
    }

    fun parseBoolean(string: String): Boolean {
        return string.toLowerCase().matches(TRUE)
    }

}