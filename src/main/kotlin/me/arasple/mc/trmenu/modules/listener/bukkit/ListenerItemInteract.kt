package me.arasple.mc.trmenu.modules.listener.bukkit

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.lite.cooldown.Cooldown
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.service.Mirror
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

/**
 * @author Arasple
 * @date 2020/3/17 22:00
 */
@TListener
class ListenerItemInteract : Listener {

    private val cooldown = Cooldown("BIND_ITEM", 2)

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        Mirror.eval("Listener:onInteractItem(async)") {
            if (Version.isAfter(Version.v1_9) && e.hand == EquipmentSlot.OFF_HAND) return@eval
            val player = e.player
            val item = e.item ?: return@eval

            if (player.openInventory.topInventory.holder != player.inventory.holder) {
                return@eval
            }
            val menu = Menu.getMenus().find { it.settings.bindings.matchItem(player, item) }
            if (menu != null && !cooldown.isCooldown(player.name)) {
                e.isCancelled = true
                menu.open(player, -1, MenuOpenEvent.Reason.BINDING_ITEMS)
            }
        }
    }

}