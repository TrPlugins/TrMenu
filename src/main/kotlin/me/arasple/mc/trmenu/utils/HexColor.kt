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
object HexColor {

    val ENABLED = Version.isAfter(Version.v1_16)
    val PATTERN_HEX: Pattern = Pattern.compile("&<([0-9a-fA-F]{6})>")
    val PATTERN_RGB: Pattern = Pattern.compile("&<([0-9]+[,]+[0-9]+[,]+[0-9]*)>")

    fun translate(list: List<String>) = if (!ENABLED) list else mutableListOf<String>().let { result ->
        list.forEach { result.add(translate(it)) }
        return@let result
    }

    fun translate(message: String): String {
        if (!ENABLED) return message

        val hex = PATTERN_HEX.matcher(message).let {
            val buffer = StringBuffer(message.length + 4 * 8)
            while (it.find()) it.appendReplacement(buffer, ChatColor.of("#${it.group(1)}").toString())
            return@let it.appendTail(buffer).toString()
        }
        return PATTERN_RGB.matcher(hex).let {
            val buffer = StringBuffer(hex.length + 4 * 8)
            while (it.find()) it.appendReplacement(buffer, ChatColor.of(serializeRgbColor(it.group(1))).toString())
            return@let it.appendTail(buffer).toString()
        }
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