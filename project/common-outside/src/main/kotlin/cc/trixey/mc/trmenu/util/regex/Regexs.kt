package cc.trixey.mc.trmenu.util.regex

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.regex.Regexs
 *
 * @author Score2
 * @since 2022/12/31 13:09
 */
object Regexs {
    val REGEX_TRUE = "true|yes|on".toRegex()
    val REGEX_FALSE = "false|no|off".toRegex()
    val REGEX_BOOLEAN = "true|yes|on|false|no|off".toRegex()

    val REGEX_CONDITION = "(condition|requirement)s?".toRegex()

    val REGEX_PLACEHOLDER = "(%)(.+?)(%)|(?!\\{\")((\\{)(.+?)(}))".toRegex()
}