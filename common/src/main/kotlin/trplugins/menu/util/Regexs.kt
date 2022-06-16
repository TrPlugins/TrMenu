package trplugins.menu.util

/**
 * @author Arasple
 * @date 2021/1/24 16:57
 */
object Regexs {

    private val PLACEHOLDER_API = "(%)(.+?)(%)|(?!\\{\")((\\{)(.+?)(}))".toRegex()
    val JSON_TEXTURE = "\\{\\s*\"".toRegex()
    val ICON_KEY = "`(.+?)`".toRegex()
    val SENTENCE = "`(.+?)`".toRegex()
    val STRING = "\\{(\\w+)}".toRegex()

    private val TRUE = "true|yes|on".toRegex()
    val FALSE = "false|no|off".toRegex()
    val BOOLEAN = "true|yes|on|false|no|off".toRegex()

    fun containsPlaceholder(string: String): Boolean {
        return PLACEHOLDER_API.find(string) != null
    }

    fun parseBoolean(string: String): Boolean {
        return string.lowercase().matches(TRUE)
    }

}