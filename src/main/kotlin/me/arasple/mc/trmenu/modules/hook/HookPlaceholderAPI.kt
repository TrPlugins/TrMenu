package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.inject.THook
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MenuSession
import me.arasple.mc.trmenu.data.MetaPlayer
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
            "args" -> {
                val arguments = MetaPlayer.getArguments(player)
                if (params.size > 1) {
                    val range: Array<Int>
                    params[0].split("-").let {
                        range = arrayOf(it[0].toInt(), it[0].toInt())
                        if (it.size > 1) range[1] = it[1].toInt()
                    }
                    return buildString {
                        IntRange(range[0], range[1]).forEach {
                            append(arguments[it])
                            append(" ")
                        }
                    }.removeSuffix(" ")
                }
                return "null"
            }
            "meta" -> (if (params.size > 1) params[1] else null)?.let { MetaPlayer.getMeta(player, it) }?.toString() ?: "null"
            "menu" -> {
                val session = MenuSession.session(player)
                if (!session.isNull()) {
                    when (params[1]) {
                        "page" -> session.page
                        "next" -> session.page
                        "title" -> session.menu!!.settings.title.currentTitle(player)
                    }
                }
                ""
            }
            else -> ""
        }
    }

    @THook
    class Inject : PlaceholderExpansion() {

        override fun getIdentifier() = "trmenu"

        override fun getVersion() = TrMenu.plugin.description.version

        override fun getAuthor() = "Arasple"

        override fun persist() = true

        override fun onPlaceholderRequest(plauer: Player, content: String) = processRequest(plauer, content)

    }

}