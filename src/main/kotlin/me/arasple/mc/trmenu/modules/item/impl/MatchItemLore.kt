package me.arasple.mc.trmenu.modules.item.impl

import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:11
 */
class MatchItemLore : MatchItemIdentifier("(display)?(-)?lore(s)?") {

	override fun match(player: Player, itemStack: ItemStack): Boolean = itemStack.itemMeta.let { it ->
		if (it != null) {
			val lore = it.lore
			if (lore != null) return lore.any { it.contains(getContent(player), true) }
		}
		return false
	}

}