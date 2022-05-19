package trplugins.menu.api.action.impl.func

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.util.bukkit.ItemHelper

/**
 * TrMenu
 * trplugins.menu.api.action.impl.func.RepairItem
 *
 * @author Rubenicos
 * @since 2022/02/14 13:16
 */
class RepairItem(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "repair(-)?items?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach {
            repair(ItemHelper.fromPlayerInv(player.cast<Player>().inventory, it))
        }
        player.cast<Player>().updateInventory()
    }

    private fun repair(any: Any?) {
        if (any is Array<*>) {
            any.forEach { repairItem(it as ItemStack?) }
        } else if (any is ItemStack) repairItem(any)
    }

    @Suppress("DEPRECATION")
    private fun repairItem(item: ItemStack?) {
        if (item == null || item.type == Material.AIR || item.type.isBlock || item.type.isEdible || item.type.maxDurability <= 0 || item.durability == (0).toShort())
            return
        item.durability = (0).toShort()
    }
}