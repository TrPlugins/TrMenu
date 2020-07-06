package me.arasple.mc.trmenu.modules.item.impl

import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:06
 */
class MatchItemName : MatchItemIdentifier("(display)?(-)?name(s)?") {

	override fun match(player: Player, itemStack: ItemStack): Boolean = itemStack.itemMeta.let {
		if (it != null) {
			val displayName = it.displayName
			return displayName.equals(getContent(player), true)
		}
		return false
	}

}