package me.arasple.mc.trmenu.api.action.impl.func

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Rubenicos
 * @date 2021/11/09 17:46
 */
class ActionRepairItem(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer, ";").forEach {
            repair(ItemHelper.fromPlayerInv(player.inventory, it))
        }
        player.updateInventory()
    }

    companion object {

        private val name = "repair(-)?items?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionRepairItem(value.toString(), option)
        }

        val registery = name to parser

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
}