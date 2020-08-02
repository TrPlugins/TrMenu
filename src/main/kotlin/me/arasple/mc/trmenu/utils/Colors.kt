package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.Version
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import net.md_5.bungee.api.ChatColor
import java.awt.Color
import java.util.regex.Pattern
import kotlin.math.min


/**
 * @author Arasple
 * @date 2020/7/7 20:59
 */
object Colors {

    val PATTERN_HEX: Pattern = Pattern.compile("[&#][<{]([0-9a-fA-F]{6})[>}]")
    val PATTERN_RGB: Pattern = Pattern.compile("[&#][<{]([0-9]+[,]+[0-9]+[,]+[0-9]*)[>}]")
    val ENABLED = Version.isAfter(Version.v1_16)

    fun translate(list: List<String>) = if (!ENABLED) list else list.map { translate(it) }

    fun translate(message: String): String = replace(PATTERN_RGB, replace(PATTERN_HEX, message, false), true)

    private fun replace(pattern: Pattern, message: String, rgb: Boolean) =
        pattern.matcher(message).let {
            val buffer = StringBuffer(message.length + 4 * 8)
            while (it.find()) {
                val replacement = if (rgb) ChatColor.of(serializeRgbColor(it.group(1))) else ChatColor.of("#${it.group(1)}")
                it.appendReplacement(buffer, replacement.toString())
            }
            return@let it.appendTail(buffer).toString()
        }

    fun serializeRgbColor(color: String): Color {
        val rgb = color.split(",").toTypedArray()
        if (rgb.size == 3) {
            val r = min(NumberUtils.toInt(rgb[0], 0), 255)
            val g = min(NumberUtils.toInt(rgb[1], 0), 255)
            val b = min(NumberUtils.toInt(rgb[2], 0), 255)
            return Color(r, g, b)
        }
        return Color.BLACK
    }

}