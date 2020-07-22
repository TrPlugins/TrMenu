package me.arasple.mc.trmenu.display.function

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Patterns.INTERNAL_FUNCTION
import org.bukkit.entity.Player
import java.util.regex.Matcher

/**
 * @author Arasple
 * @date 2020/2/28 22:51
 */
data class InternalFunction(val id: String, val value: String) {

    companion object {

        fun match(string: String): Matcher = INTERNAL_FUNCTION.matcher(string)

        fun replaceWithFunctions(player: Player, functions: Set<InternalFunction>, string: String): String {
            val buffer = StringBuffer(string.length)
            return match(string).let { matcher ->
                while (matcher.find()) {
                    val group = matcher.group(1).split("_").toTypedArray()
                    functions.firstOrNull { it.id.equals(group[0], true) }?.let {
                        matcher.appendReplacement(buffer, it.eval(player, ArrayUtils.remove(group, 0)))
                    }
                }
                matcher.appendTail(buffer).toString()
            }
        }

    }

    fun eval(player: Player, args: Array<String>): String = Strings.replaceWithOrder(value, *args).let {
        return@let Scripts.script(player, it, true).asString()
    }

}