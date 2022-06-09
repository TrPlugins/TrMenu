package trplugins.menu.module.internal.hook.ext

import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.display.Menu
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.script.js.JavaScriptAgent
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.pluginId
import taboolib.platform.compat.PlaceholderExpansion
import taboolib.platform.util.sendLang
import trplugins.menu.TrMenu
import trplugins.menu.util.ignoreCase

/**
 * @author Arasple
 * @date 2021/2/4 11:37
 */
object HookPlaceholderAPI : PlaceholderExpansion {

    val enabledParseJavaScript get() = TrMenu.SETTINGS.getBoolean("Options.Placeholders.JavaScript-Parse", false)

    override val identifier = pluginId

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        if (player?.isOnline == true) {
            val session = MenuSession.getSession(player)
            val args = params.split("_")
            val key = args.getOrElse(1) { "" }
            val value = { args.slice(1 until args.size).joinToString("_") }

            return kotlin.runCatching { when (args[0].lowercase()) {
                "menus" -> Menu.menus.size
                "args" -> session.arguments[key.toIntOrNull() ?: 0]
                "meta" -> Metadata.getMeta(player)[key]
                "data" -> Metadata.getData(player)[key]
                "globaldata" -> runCatching { Metadata.globalData[value()].toString() }.getOrElse { "null" }
                "node" -> session.menu?.conf?.let { it[it.ignoreCase(value())].toString() }.toString()
                "menu" -> menu(session, args)
                "js" -> if (enabledParseJavaScript) if (args.size > 1) JavaScriptAgent.eval(session, args[1]).asString() else "" else "UNABLE_PARSE"
                else -> ""
            } }.getOrNull().toString()
        }

        return "__"
    }

    private fun menu(session: MenuSession, params: List<String>): String {
        return when (params[1]) {
            "page" -> session.page.toString()
            "pages" -> session.menu?.layout?.layouts?.size.toString()
            "next" -> (session.page + 1).toString()
            "prev" -> (session.page - 1).toString()
            "title" -> session.menu?.settings?.title?.get(session.id).toString()
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
            viewer.sendLang("Menu-Expansions-Header", expansions.size)
            expansions.forEach { viewer.sendLang("Menu-Expansions-Format", it) }
        }
    }

}