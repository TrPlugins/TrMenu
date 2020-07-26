package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.db.local.LocalPlayer
import io.izzel.taboolib.module.inject.THook
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MetaPlayer.getArguments
import me.arasple.mc.trmenu.data.MetaPlayer.getMeta
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Utils
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
            "args" -> arguments(player, params)
            "meta" -> meta(player, params)
            "data" -> if (params.size > 1) LocalPlayer.get(player).getString("TrMenu.Data.${params[1]}", "")!! else ""
            "menu" -> menu(player, params)
            "emptyslot" -> freeSlot(player, params)
            "js" -> if (params.size > 1) Scripts.script(player, params[1], true).asString() else ""
            else -> ""
        }
    }

    private fun arguments(player: Player, params: List<String>): String {
        val arguments = player.getArguments()
        if (params.size > 1) {
            return buildString {
                Utils.asIntRange(params[1]).forEach {
                    append(arguments[it])
                    append(" ")
                }
            }.removeSuffix(" ")
        }
        return "null"
    }

    private fun meta(player: Player, params: List<String>): String {
        return (if (params.size > 1) params[1] else null)?.let { player.getMeta(it) }?.toString() ?: "null"
    }

    private fun menu(player: Player, params: List<String>): String {
        val session = player.getMenuSession()
        return if (!session.isNull()) {
            when (params[1]) {
                "page" -> session.page.toString()
                "next" -> session.page.toString()
                "title" -> session.menu!!.settings.title.currentTitle(player)
                else -> ""
            }
        } else ""
    }

    private fun freeSlot(player: Player, params: List<String>): String {
        val session = player.getMenuSession()
        // trmenu_emptyslot_0_1-10
        // trmenu_emptyslot_0_1-10
        val range = Utils.asIntRange(if (params.size > 2) params[2] else "0-90")
        val index = if (params.size > 1) params[1].toInt() else 0
        return (session.menu?.getEmptySlots(player, session.page)?.filter { range.contains(it) }?.get(index) ?: -1).toString()
    }


    @THook
    class Inject : PlaceholderExpansion() {

        override fun getIdentifier() = "trmenu"

        override fun getVersion() = TrMenu.plugin.description.version

        override fun getAuthor() = "Arasple"

        override fun persist() = true

        override fun onPlaceholderRequest(player: Player, content: String) = processRequest(player, content)

    }

}