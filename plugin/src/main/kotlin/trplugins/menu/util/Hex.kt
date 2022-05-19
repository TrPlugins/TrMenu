package trplugins.menu.util

import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import taboolib.module.nms.MinecraftVersion
import java.awt.Color
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.math.*

/**
 * @author Esophose
 * converted from https://github.com/Rosewood-Development/RoseGarden/blob/master/src/main/java/dev/rosewood/rosegarden/utils/HexUtils.java
 */
object Hex {
    private const val CHARS_UNTIL_LOOP = 30
    private val RAINBOW_PATTERN =
        Pattern.compile("<(?<type>rainbow|r)(#(?<speed>\\d+))?(:(?<saturation>\\d*\\.?\\d+))?(:(?<brightness>\\d*\\.?\\d+))?(:(?<loop>l|L|loop))?>")
    private val GRADIENT_PATTERN =
        Pattern.compile("<(?<type>gradient|g)(#(?<speed>\\d+))?(?<hex>(:#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})){2,})(:(?<loop>l|L|loop))?>")
    private val HEX_PATTERNS = listOf(
        Pattern.compile("<#([A-Fa-f0-9]){6}>"),  // <#FFFFFF>
        Pattern.compile("\\{#([A-Fa-f0-9]){6}}"),  // {#FFFFFF}
        Pattern.compile("&#([A-Fa-f0-9]){6}"),  // &#FFFFFF
        Pattern.compile("#([A-Fa-f0-9]){6}") // #FFFFFF
    )
    private val STOP = Pattern.compile(
        "<(rainbow|r)(#(\\d+))?(:(\\d*\\.?\\d+))?(:(\\d*\\.?\\d+))?(:(l|L|loop))?>|" +
                "<(gradient|g)(#(\\d+))?((:#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})){2,})(:(l|L|loop))?>|" +
                "(&[a-f0-9r])|" +
                "<#([A-Fa-f0-9]){6}>|" +
                "\\{#([A-Fa-f0-9]){6}}|" +
                "&#([A-Fa-f0-9]){6}|" +
                "#([A-Fa-f0-9]){6}|" +
                org.bukkit.ChatColor.COLOR_CHAR
    )

    /**
     * Gets a capture group from a regex Matcher if it exists
     *
     * @param matcher The Matcher
     * @param group The group name
     * @return the capture group value, or null if not found
     */
    private fun getCaptureGroup(matcher: Matcher, group: String): String? {
        return try {
            matcher.group(group)
        } catch (e: IllegalStateException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Sends a CommandSender a colored message
     *
     * @param sender  The CommandSender to send to
     * @param message The message to send
     */
    fun sendMessage(sender: CommandSender, message: String) {
        sender.sendMessage(colorify(message))
    }

    /**
     * Parses gradients, hex colors, and legacy color codes
     *
     * @param message The message
     * @return A color-replaced message
     */
    fun colorify(message: String): String {
        var parsed = message
        parsed = parseRainbow(parsed)
        parsed = parseGradients(parsed)
        parsed = parseHex(parsed)
        parsed = parseLegacy(parsed)
        return parsed
    }

    internal fun parseRainbow(message: String): String {
        var parsed = message
        var matcher = RAINBOW_PATTERN.matcher(parsed)
        while (matcher.find()) {
            val parsedRainbow = StringBuilder()

            // Possible parameters and their defaults
            var speed = -1
            var saturation = 1.0f
            var brightness = 1.0f
            val looping = getCaptureGroup(matcher, "looping") != null
            val speedGroup = getCaptureGroup(matcher, "speed")
            if (speedGroup != null) {
                try {
                    speed = speedGroup.toInt()
                } catch (ignored: NumberFormatException) {
                }
            }
            val saturationGroup = getCaptureGroup(matcher, "saturation")
            if (saturationGroup != null) {
                try {
                    saturation = saturationGroup.toFloat()
                } catch (ignored: NumberFormatException) {
                }
            }
            val brightnessGroup = getCaptureGroup(matcher, "brightness")
            if (brightnessGroup != null) {
                try {
                    brightness = brightnessGroup.toFloat()
                } catch (ignored: NumberFormatException) {
                }
            }
            val stop = findStop(parsed, matcher.end())
            val content = parsed.substring(matcher.end(), stop)
            var contentLength = content.length
            val chars = content.toCharArray()
            for (i in 0 until chars.size - 1) if (chars[i] == '&' && "KkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) contentLength -= 2
            val length = if (looping) Math.min(contentLength, CHARS_UNTIL_LOOP) else contentLength
            var rainbow: ColorGenerator
            rainbow = if (speed == -1) {
                Rainbow(length, saturation, brightness)
            } else {
                AnimatedRainbow(length, saturation, brightness, speed)
            }
            var compoundedFormat: String = "" // Carry the format codes through the rainbow gradient
            var i = 0
            while (i < chars.size) {
                val c = chars[i]
                if (c == '&' && i + 1 < chars.size) {
                    val next = chars[i + 1]
                    val color = org.bukkit.ChatColor.getByChar(next)
                    if (color != null && color.isFormat) {
                        compoundedFormat += ChatColor.COLOR_CHAR.toString() + next
                        i++ // Skip next character
                        i++
                        continue
                    }
                }
                parsedRainbow.append(translateHex(rainbow.next())).append(compoundedFormat).append(c)
                i++
            }
            val before = parsed.substring(0, matcher.start())
            val after = parsed.substring(stop)
            parsed = before + parsedRainbow + after
            matcher = RAINBOW_PATTERN.matcher(parsed)
        }
        return parsed
    }

    internal fun parseGradients(message: String): String {
        var parsed = message
        var matcher = GRADIENT_PATTERN.matcher(parsed)
        while (matcher.find()) {
            val parsedGradient = StringBuilder()
            var speed = -1
            val looping = getCaptureGroup(matcher, "loop") != null
            val hexSteps = Arrays.stream(
                getCaptureGroup(matcher, "hex")!!.substring(1).split(":").toTypedArray()
            )
                .map { x: String -> if (x.length != 4) x else String.format("#%s%s%s%s%s%s", x[1], x[1], x[2], x[2], x[3], x[3]) }
                .map { nm: String? -> Color.decode(nm) }
                .collect(Collectors.toList())
            val speedGroup = getCaptureGroup(matcher, "speed")
            if (speedGroup != null) {
                try {
                    speed = speedGroup.toInt()
                } catch (ignored: NumberFormatException) {
                }
            }
            val stop = findStop(parsed, matcher.end())
            val content = parsed.substring(matcher.end(), stop)
            var contentLength = content.length
            val chars = content.toCharArray()
            for (i in 0 until chars.size - 1) if (chars[i] == '&' && "KkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) contentLength -= 2
            val length = if (looping) Math.min(contentLength, CHARS_UNTIL_LOOP) else contentLength
            val gradient: ColorGenerator = if (speed == -1) {
                Gradient(hexSteps, length)
            } else {
                AnimatedGradient(hexSteps, length, speed)
            }
            var compoundedFormat = "" // Carry the format codes through the gradient
            var i = 0
            while (i < chars.size) {
                val c = chars[i]
                if (c == '&' && i + 1 < chars.size) {
                    val next = chars[i + 1]
                    val color = org.bukkit.ChatColor.getByChar(next)
                    if (color != null && color.isFormat) {
                        compoundedFormat += ChatColor.COLOR_CHAR.toString() + next
                        i++ // Skip next character
                        i++
                        continue
                    }
                }
                parsedGradient.append(translateHex(gradient.next())).append(compoundedFormat).append(c)
                i++
            }
            val before = parsed.substring(0, matcher.start())
            val after = parsed.substring(stop)
            parsed = before + parsedGradient + after
            matcher = GRADIENT_PATTERN.matcher(parsed)
        }
        return parsed
    }

    internal fun parseHex(message: String): String {
        var parsed = message
        for (pattern: Pattern in HEX_PATTERNS) {
            var matcher = pattern.matcher(parsed)
            while (matcher.find()) {
                val color = translateHex(cleanHex(matcher.group()))
                val before = parsed.substring(0, matcher.start())
                val after = parsed.substring(matcher.end())
                parsed = before + color + after
                matcher = pattern.matcher(parsed)
            }
        }
        return parsed
    }

    private fun parseLegacy(message: String): String {
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    /**
     * Returns the index before the color changes
     *
     * @param content     The content to search through
     * @param searchAfter The index at which to search after
     * @return the index of the color stop, or the end of the string index if none is found
     */
    private fun findStop(content: String, searchAfter: Int): Int {
        val matcher = STOP.matcher(content)
        while (matcher.find()) {
            if (matcher.start() > searchAfter) return matcher.start()
        }
        return content.length
    }

    private fun cleanHex(hex: String): String {
        return if (hex.startsWith("<") || hex.startsWith("{")) {
            hex.substring(1, hex.length - 1)
        } else if (hex.startsWith("&")) {
            hex.substring(1)
        } else {
            hex
        }
    }

    /**
     * Finds the closest hex or ChatColor value as the hex string
     *
     * @param hex The hex color
     * @return The closest ChatColor value
     */
    private fun translateHex(hex: String): String {
        return if (MinecraftVersion.majorLegacy >= 11600) ChatColor.of(hex).toString() else translateHex(Color.decode(hex))
    }

    private fun translateHex(color: Color): String {
        if (MinecraftVersion.majorLegacy >= 11600) return ChatColor.of(color).toString()
        var minDist = Int.MAX_VALUE
        var legacy = ChatColor.WHITE
        for (mapping: ChatColorHexMapping in ChatColorHexMapping.values()) {
            val r = mapping.red - color.red
            val g = mapping.green - color.green
            val b = mapping.blue - color.blue
            val dist = (r * r) + (g * g) + (b * b)
            if (dist < minDist) {
                minDist = dist
                legacy = mapping.chatColor
            }
        }
        return legacy.toString()
    }

    /**
     * Maps hex codes to ChatColors
     */
    private enum class ChatColorHexMapping(hex: Int, val chatColor: ChatColor) {
        BLACK(0x000000, ChatColor.BLACK), DARK_BLUE(0x0000AA, ChatColor.DARK_BLUE), DARK_GREEN(
            0x00AA00,
            ChatColor.DARK_GREEN
        ),
        DARK_AQUA(0x00AAAA, ChatColor.DARK_AQUA), DARK_RED(0xAA0000, ChatColor.DARK_RED), DARK_PURPLE(
            0xAA00AA,
            ChatColor.DARK_PURPLE
        ),
        GOLD(0xFFAA00, ChatColor.GOLD), GRAY(0xAAAAAA, ChatColor.GRAY), DARK_GRAY(0x555555, ChatColor.DARK_GRAY), BLUE(
            0x5555FF,
            ChatColor.BLUE
        ),
        GREEN(0x55FF55, ChatColor.GREEN), AQUA(0x55FFFF, ChatColor.AQUA), RED(0xFF5555, ChatColor.RED), LIGHT_PURPLE(
            0xFF55FF,
            ChatColor.LIGHT_PURPLE
        ),
        YELLOW(0xFFFF55, ChatColor.YELLOW), WHITE(0xFFFFFF, ChatColor.WHITE);

        val red: Int = (hex shr 16) and 0xFF
        val green: Int = (hex shr 8) and 0xFF
        val blue: Int = hex and 0xFF

    }

    private interface ColorGenerator {
        /**
         * @return the next color in the sequence
         */
        operator fun next(): Color
    }

    /**
     * Allows generation of a multi-part gradient with a defined number of steps
     */
    open class Gradient(colors: List<Color>, steps: Int) :
        ColorGenerator {
        private val gradients: MutableList<TwoStopGradient>
        private val steps: Int
        protected var step: Long
        override fun next(): Color {
            // Gradients will use the first color if the entire spectrum won't be available to preserve prettiness
            if (MinecraftVersion.majorLegacy < 11600 || steps <= 1) return gradients[0].colorAt(0)

            // Do some wizardry to get a function that bounces back and forth between 0 and a cap given an increasing input
            // Thanks to BomBardyGamer for assisting with this
            val adjustedStep = abs(((2 * asin(sin(step * (Math.PI / (2 * steps))))) / Math.PI) * steps).roundToInt()
            val color: Color = if (gradients.size < 2) {
                gradients[0].colorAt(adjustedStep)
            } else {
                val segment = steps.toFloat() / gradients.size
                val index = floor((adjustedStep / segment).toDouble()).coerceAtMost((gradients.size - 1).toDouble()).toInt()
                gradients[index].colorAt(adjustedStep)
            }
            step++
            return color
        }

        private class TwoStopGradient internal constructor(
            private val startColor: Color,
            private val endColor: Color,
            private val lowerRange: Float,
            private val upperRange: Float
        ) {
            /**
             * Gets the color of this gradient at the given step
             *
             * @param step The step
             * @return The color of this gradient at the given step
             */
            fun colorAt(step: Int): Color {
                return Color(
                    calculateHexPiece(step, startColor.red, endColor.red),
                    calculateHexPiece(step, startColor.green, endColor.green),
                    calculateHexPiece(step, startColor.blue, endColor.blue)
                )
            }

            private fun calculateHexPiece(step: Int, channelStart: Int, channelEnd: Int): Int {
                val range = upperRange - lowerRange
                val interval = (channelEnd - channelStart) / range
                return Math.round(interval * (step - lowerRange) + channelStart)
            }
        }

        init {
            if (colors.size < 2) throw IllegalArgumentException("Must provide at least 2 colors")
            gradients = ArrayList()
            this.steps = steps - 1
            step = 0
            val increment = this.steps.toFloat() / (colors.size - 1)
            for (i in 0 until colors.size - 1) gradients.add(
                TwoStopGradient(
                    colors[i],
                    colors[i + 1], increment * i, increment * (i + 1)
                )
            )
        }
    }

    /**
     * Allows generation of an animated multi-part gradient with a defined number of steps
     */
    class AnimatedGradient(colors: List<Color>, steps: Int, speed: Int) :
        Gradient(colors, steps) {
        init {
            step = System.currentTimeMillis() / speed
        }
    }

    /**
     * Allows generation of a rainbow gradient with a fixed number of steps
     */
    open class Rainbow(totalColors: Int, saturation: Float, brightness: Float) :
        ColorGenerator {
        protected val hueStep: Float
        protected val saturation: Float
        protected val brightness: Float
        protected var hue: Float
        override fun next(): Color {
            val color = Color.getHSBColor(hue, saturation, brightness)
            hue += hueStep
            return color
        }

        init {
            var totalColors = totalColors
            if (totalColors < 1) totalColors = 1
            hueStep = 1.0f / totalColors
            this.saturation = 0f.coerceAtLeast(1f.coerceAtMost(saturation))
            this.brightness = 0f.coerceAtLeast(1f.coerceAtMost(brightness))
            hue = 0f
        }
    }

    /**
     * Allows generation of an animated rainbow gradient with a fixed number of steps
     */
    class AnimatedRainbow(totalColors: Int, saturation: Float, brightness: Float, speed: Int) : Rainbow(totalColors, saturation, brightness) {
        init {
            hue = ((((floor(System.currentTimeMillis() / 50.0)) / 360) * speed) % 1).toFloat()
        }
    }
}

fun String.parseRainbow() = Hex.parseRainbow(this)
fun String.parseGradients() = Hex.parseGradients(this)