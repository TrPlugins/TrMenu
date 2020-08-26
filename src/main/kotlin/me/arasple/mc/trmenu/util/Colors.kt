package me.arasple.mc.trmenu.util

import io.izzel.taboolib.Version
import net.md_5.bungee.api.ChatColor
import java.awt.Color
import java.util.regex.Pattern
import kotlin.math.min


/**
 * @author Arasple
 * @date 2020/7/7 20:59
 */
object Colors {

    val PATTERN_HEX: Pattern = Pattern.compile("[&#]?[<{](#)?([0-9a-fA-F]{6})[>}]")
    val PATTERN_RGB: Pattern = Pattern.compile("[&#][<{]([0-9]+[,]+[0-9]+[,]+[0-9]*)[>}]")
    val PATTERN_GRADIENT: Pattern = Pattern.compile("[<{](gradient|g)([:=,;]#[0-9a-fA-F]{6})+[>}]")
    val PATTERN_GRADIENT_STOP: Pattern = Pattern.compile("[<{](gradient|g)[>}]")

    val ENABLED = Version.isAfter(Version.v1_16)

    fun translate(message: String): String {
        return if (!ENABLED) message else replace(PATTERN_RGB, replace(PATTERN_HEX, parseGradients(message), false), true)
    }

    fun translate(list: List<String>): List<String> {
        return if (!ENABLED) list else list.map { translate(it) }
    }

    private fun replace(pattern: Pattern, message: String, rgb: Boolean) =
        pattern.matcher(message).let {
            val buffer = StringBuffer(message.length + 4 * 8)
            while (it.find()) {
                val replacement = if (rgb) ChatColor.of(serializeRgbColor(it.group(1))) else ChatColor.of("#${it.group(1).removePrefix("#")}")
                it.appendReplacement(buffer, replacement.toString())
            }
            return@let it.appendTail(buffer).toString()
        }

    fun serializeRgbColor(color: String): Color {
        val rgb = color.split(",").toTypedArray()
        if (rgb.size == 3) {
            val r = min(rgb[0].toIntOrNull() ?: 0, 255)
            val g = min(rgb[1].toIntOrNull() ?: 0, 255)
            val b = min(rgb[2].toIntOrNull() ?: 0, 255)
            return Color(r, g, b)
        }
        return Color.BLACK
    }

    /**
     * from https://github.com/harry0198/HexiTextLib
     */
    fun parseGradients(text: String): String {
        try {
            var parsed = text
            var matcher = PATTERN_GRADIENT.matcher(parsed)
            while (matcher.find()) {
                val parsedGradient = StringBuilder()
                val match = matcher.group()
                val tagLength = if (match.startsWith("<gr") || match.startsWith("{gr")) 10 else 3
                val indexOfClose = if (match.indexOf(">") < 0) match.indexOf("}") else match.indexOf(">")
                val hexContent = match.substring(tagLength, indexOfClose)
                val hexSteps = hexContent.split(":", ",", ";").map { Color.decode(it) }
                val stop = findGradientStop(parsed, matcher.end())
                val content = parsed.substring(matcher.end(), stop)
                val gradient = Gradient(hexSteps, content.length)
                for (c in content.toCharArray()) parsedGradient.append(ChatColor.of(gradient.next()).toString()).append(c)
                val before = parsed.substring(0, matcher.start())
                val after = parsed.substring(stop)
                parsed = before + parsedGradient + after
                matcher = PATTERN_GRADIENT.matcher(parsed)
            }
            return parsed
        } catch (e: Throwable) {
            println("Error parsing gradients for text: $text")
            return text
        }
    }

    fun findGradientStop(content: String, searchAfter: Int): Int {
        val matcher = PATTERN_GRADIENT_STOP.matcher(content)
        while (matcher.find()) {
            if (matcher.start() > searchAfter) return matcher.start()
        }
        return content.length - 1
    }

    /**
     * Gets a color along a linear gradient between two colors
     *
     * @param start The start color
     * @param end The end color
     * @param interval The interval to get, between 0 and 1 inclusively
     * @return A Color at the interval between the start and end colors
     */
    fun getGradientInterval(start: Color, end: Color, interval: Float): Color {
        require(!(0 > interval || interval > 1)) { "Interval must be between 0 and 1 inclusively." }
        val r = (end.red * interval + start.red * (1 - interval)).toInt()
        val g = (end.green * interval + start.green * (1 - interval)).toInt()
        val b = (end.blue * interval + start.blue * (1 - interval)).toInt()
        return Color(r, g, b)
    }

    /**
     * Gradient class
     * Derived from:
     * https://github.com/Rosewood-Development/RoseStacker/blob/master/Plugin/src/main/java/dev/rosewood/rosestacker/utils/HexUtils.java
     * https://github.com/Oribuin/ChatEmojis/blob/master/src/main/java/xyz/oribuin/chatemojis/utils/HexUtils.java
     *
     * Unsure who original author is.
     * @author unknown
     */
    class Gradient(colors: List<Color>, totalColors: Int) {

        private val colors: List<Color>
        private val stepSize: Int
        private var step: Int
        private var stepIndex: Int

        init {
            require(colors.size >= 2) { "Must provide at least 2 colors" }
            require(totalColors >= 1) { "Must have at least 1 total color" }
            this.colors = colors
            stepSize = totalColors / (colors.size - 1)
            stepIndex = 0
            step = stepIndex
        }

        /**
         * @return the next color in the gradient
         */
        operator fun next(): Color {
            val color: Color
            color = if (stepIndex + 1 < colors.size) {
                val start: Color = colors[stepIndex]
                val end: Color = colors[stepIndex + 1]
                val interval = step.toFloat() / stepSize
                getGradientInterval(start, end, interval)
            } else {
                colors[colors.size - 1]
            }
            step += 1
            if (step >= stepSize) {
                step = 0
                stepIndex++
            }
            return color
        }

    }

}