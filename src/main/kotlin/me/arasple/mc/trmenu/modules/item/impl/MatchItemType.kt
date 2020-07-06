package me.arasple.mc.trmenu.modules.item.impl

import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/3/13 22:11
 */
class MatchItemType : MatchItemIdentifier("(type|mat(erial)?)(s)?") {

	override fun match(player: Player, itemStack: ItemStack): Boolean = itemStack.type.name == getContent(player).toUpperCase(Locale.ENGLISH).replace(Regex("( )|-"), "_")

}