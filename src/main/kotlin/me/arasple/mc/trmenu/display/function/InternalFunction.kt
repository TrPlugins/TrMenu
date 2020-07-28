package me.arasple.mc.trmenu.display.function

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.modules.script.Scripts
import org.bukkit.entity.Player
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/2/28 22:51
 */
data class InternalFunction(val id: String, val value: String) {

    companion object {

        val PATTERN: Pattern = Pattern.compile("\\\$\\{(.*?)}")

        fun match(string: String): Matcher = PATTERN.matcher(string)

    }

    fun eval(player: Player, args: Array<String>): String = Strings.replaceWithOrder(value, *args).let {
        return@let Scripts.script(player, it, true).asString()
    }

}