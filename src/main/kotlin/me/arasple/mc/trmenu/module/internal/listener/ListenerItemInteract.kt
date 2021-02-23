package me.arasple.mc.trmenu.module.internal.listener

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.lite.cooldown.Cooldown
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * @author Arasple
 * @date 2021/1/29 17:18
 */
@TListener
class ListenerItemInteract : Listener {

    private val interactCooldown by lazy {

        Cooldown("BOUND_ITEM", TrMenu.SETTINGS.getInt("Menu.Settings.Bound-Item-Interval"))
            .also { it.plugin = "TrMenu" }

    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (Version.isAfter(Version.v1_9) && e.hand == EquipmentSlot.OFF_HAND) return
        val player = e.player
        val item = e.item ?: return
        val session = MenuSession.getSession(player)

        if (player.openInventory.topInventory.holder != player.inventory.holder || session.menu != null) return
        if (interactCooldown.isCooldown(player.name)) return

        Tasks.task(true) {
            val menu = Menu.menus.find { it -> it.settings.boundItems.any { it.itemMatches(item) } }
            if (menu != null) {
                e.isCancelled = true
                menu.open(player, reason = MenuOpenEvent.Reason.BINDING_ITEMS)
            }
        }
    }

}