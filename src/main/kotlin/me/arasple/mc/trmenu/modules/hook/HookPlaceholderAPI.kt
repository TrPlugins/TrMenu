package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/4/4 13:43
 */
object HookPlaceholderAPI {

    fun replace(plauer: Player, content: String): String = PlaceholderAPI.setPlaceholders(plauer, PlaceholderAPI.setBracketPlaceholders(plauer, content))

    fun replace(plauer: Player, content: List<String>): List<String> = PlaceholderAPI.setPlaceholders(plauer, PlaceholderAPI.setBracketPlaceholders(plauer, content))

    private fun processRequest(player: Player, content: String): String {
        val params = content.split("_")

        return when (params[0].toLowerCase()) {
//			"args" -> {
//				val arguments = Temp.getArguments(player)
//				val index = NumberUtils.toInt(if (params.size > 1) params[1] else "0", 0)
//				if (arguments.size > index) arguments[index] else "null"
//			}
//			"temp", "var" -> (if (params.size > 1) params[1] else null)?.let { Temp.getTempVariable(player, it) } ?: "null"
            else -> ""
        }
    }

    @THook
    class Inject : PlaceholderExpansion() {

        override fun getIdentifier(): String = "trmenu"

        override fun getVersion(): String = "2.0"

        override fun getAuthor(): String = "Arasple"

        override fun persist(): Boolean = true

        override fun onPlaceholderRequest(plauer: Player, content: String): String = processRequest(plauer, content)

    }

}