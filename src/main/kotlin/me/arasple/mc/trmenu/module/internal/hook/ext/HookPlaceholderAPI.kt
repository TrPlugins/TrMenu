package me.arasple.mc.trmenu.module.internal.hook.ext

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.script.js.JavaScriptAgent
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import taboolib.common.platform.SubscribeEvent
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/2/4 11:37
 */
object HookPlaceholderAPI : PlaceholderExpansion() {

    override fun getIdentifier() = "trmenu"

    override fun getVersion() = TrMenu.plugin.description.version

    override fun getAuthor() = "Arasple"

    override fun persist() = true

    override fun onPlaceholderRequest(player: Player, params: String): String {
        if (player.isOnline) {
            val session = MenuSession.getSession(player)
            val args = params.split("_")
            val key = args.getOrElse(1) { "" }

            return when (args[0].lowercase()) {
                "menus" -> Menu.menus.size
                "args" -> session.arguments[key.toIntOrNull() ?: 0]
                "meta" -> Metadata.getMeta(player)[key]
                "data" -> Metadata.getData(player)[key]
                "menu" -> menu(session, args)
                "js" -> if (args.size > 1) JavaScriptAgent.eval(session, args[1]) else ""
                else -> ""
            }.toString()
        }

        return "__"
    }

    private fun menu(session: MenuSession, params: List<String>): String {
        return when (params[1]) {
            "page" -> session.page.toString()
            "pages" -> session.menu?.layout?.layouts?.size.toString()
            "next" -> (session.page + 1).toString()
            "prev" -> (session.page - 1).toString()
            "title" -> session.menu!!.settings.title[session.id]
            else -> ""
        }
    }

    @SubscribeEvent
    fun onMenuOpen(e: MenuOpenEvent) {
        val menu = e.menu
        val viewer = e.session.viewer
        val expansions = menu.settings.dependExpansions

        Metadata.getMeta(viewer)["open_event_reason"] = e.reason.name

        if (expansions.isNotEmpty()) {
            e.isCancelled = true
            viewer.sendLang("Menu.Expansions.Header", expansions.size)
            expansions.forEach { viewer.sendLang("Menu.Expansions.Format", it) }
        }
    }

}