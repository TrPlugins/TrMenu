package me.arasple.mc.trmenu.utils

import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/7/20 14:36
 */
object Patterns {

    val INTERNAL_FUNCTION: Pattern = Pattern.compile("\\\$\\{(.*?)}")
    val ICON_KEY_MATCHER: Pattern = Pattern.compile("`.*?`")
    val PATTERN_HEX: Pattern = Pattern.compile("&[<{]([0-9a-fA-F]{6})[>}]")
    val PATTERN_RGB: Pattern = Pattern.compile("&[<{]([0-9]+[,]+[0-9]+[,]+[0-9]*)[>}]")

}