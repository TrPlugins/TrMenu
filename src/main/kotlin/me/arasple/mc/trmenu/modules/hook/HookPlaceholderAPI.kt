package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.db.local.LocalPlayer
import io.izzel.taboolib.module.inject.THook
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Files
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MetaPlayer.getArguments
import me.arasple.mc.trmenu.data.MetaPlayer.getMeta
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.modules.script.Scripts
import me.arasple.mc.trmenu.utils.Utils
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.io.File

/**
 * @author Arasple
 * @date 2020/4/4 13:43
 */
object HookPlaceholderAPI {

    fun replace(player: OfflinePlayer, content: String): String = PlaceholderAPI.setPlaceholders(player, PlaceholderAPI.setBracketPlaceholders(player, content))

    fun replace(player: OfflinePlayer, content: List<String>): List<String> = PlaceholderAPI.setPlaceholders(player, PlaceholderAPI.setBracketPlaceholders(player, content))

    fun installDepend(): Boolean {
        val plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")
        val jarFile = File("plugins/PlaceholderAPI.jar")
        val url = "https://api.spiget.org/v2/resources/6245/download"
        if (plugin == null) {
            jarFile.delete()
            TLocale.sendToConsole("PLUGIN.DEPEND.DOWNLOAD", "PlaceholderAPI")
            if (Files.downloadFile(url, jarFile)) TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL", "PlaceholderAPI")
            else TLocale.sendToConsole("PLUGIN.DEPEND.INSTALL-FAILED", "PlaceholderAPI")
            Bukkit.shutdown()
            return true
        }
        return false
    }

    private fun processRequest(player: OfflinePlayer, content: String): String {
        if (player !is Player) return ""
        val params = content.split("_")

        return when (params[0].toLowerCase()) {
            "menus" -> Menu.getMenus().size.toString()
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
                Utils.asIntRange(params[1]).forEach { it ->
                    arguments.getOrNull(it)?.let {
                        append(it)
                        append(" ")
                    }
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
                "pages" -> session.menu?.layout?.layouts?.size.toString()
                "next" -> (session.page + 1).toString()
                "title" -> session.menu!!.settings.title.currentTitle(player)
                else -> ""
            }
        } else ""
    }

    private fun freeSlot(player: Player, params: List<String>): String {
        val session = player.getMenuSession()
        val index = NumberUtils.toInt(params.getOrElse(1) { "0" }, 0)
        val range = Utils.asIntRange(params.getOrElse(2) { "0-90" })
        return (session.menu?.getEmptySlots(player, session.page)?.filter { range.contains(it) }?.get(index) ?: -1).toString()
    }


    @THook
    class Inject : PlaceholderExpansion() {

        override fun getIdentifier() = "trmenu"

        override fun getVersion() = TrMenu.plugin.description.version

        override fun getAuthor() = "Arasple"

        override fun persist() = true

        override fun onRequest(player: OfflinePlayer?, params: String) = player?.let { processRequest(it, params) }


    }

}