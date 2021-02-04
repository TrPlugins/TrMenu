package me.arasple.mc.trmenu.module.internal.hook.ext

import io.izzel.taboolib.module.inject.THook
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2021/2/4 11:37
 */
@THook
@TListener
class HookPlaceholderAPI : PlaceholderExpansion(), Listener {

    override fun getIdentifier(): String {
        TODO("Not yet implemented")
    }

    override fun getAuthor(): String {
        TODO("Not yet implemented")
    }

    override fun getVersion(): String {
        TODO("Not yet implemented")
    }

    @EventHandler
    fun onMenuOpen(e: MenuOpenEvent) {
        val menu = e.session.menu ?: return
        val viewer = e.session.viewer
        val expansions = menu.settings.dependExpasions

        Metadata.getMeta(viewer)["open_event_reason"] = e.reason.name

        if (expansions.isNotEmpty()) {
            e.isCancelled = true
            TLocale.sendTo(viewer, "Menu.Expansions.Header", expansions.size)
            expansions.forEach {
                TLocale.sendTo(viewer, "Menu.Expansions.Format", it)
            }
        }
    }

}