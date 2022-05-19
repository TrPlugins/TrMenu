package trplugins.menu.module.internal.listener

import trplugins.menu.TrMenu
import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.display.Menu
import trplugins.menu.module.display.MenuSession
import trplugins.menu.util.reloadable
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Baffle
import taboolib.module.nms.MinecraftVersion
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2021/1/29 17:18
 */
object ListenerItemInteract {

    internal val interactCooldown = reloadable {
        Baffle.of(TrMenu.SETTINGS.getLong("Menu.Settings.Bound-Item-Interval", 2000), TimeUnit.MILLISECONDS)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onInteract(e: PlayerInteractEvent) {
        ListenerItemInteract::interactCooldown.get()

        if (MinecraftVersion.majorLegacy >= 10900 && e.hand == EquipmentSlot.OFF_HAND) return
        val player = e.player
        val item = e.item ?: return
        val session = MenuSession.getSession(player)

        if (player.openInventory.topInventory.holder != (player.inventory as Inventory).holder || session.menu != null) return
        if (interactCooldown.value.hasNext(player.name)) {
            val menu = Menu.menus.find { it -> it.settings.boundItems.any { it.itemMatches(item, true) } }
            if (menu != null) {
                e.isCancelled = true
                menu.open(player, reason = MenuOpenEvent.Reason.BINDING_ITEMS)
            }
        }
    }

}