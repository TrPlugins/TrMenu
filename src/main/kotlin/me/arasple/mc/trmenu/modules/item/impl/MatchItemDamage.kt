package me.arasple.mc.trmenu.modules.item.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:17
 */
class MatchItemDamage : MatchItemIdentifier("(dat(a)?|damage)(s)?") {

	@Suppress("DEPRECATION")
	override fun match(player: Player, itemStack: ItemStack): Boolean = itemStack.durability == NumberUtils.toShort(getContent(player), 0)

}