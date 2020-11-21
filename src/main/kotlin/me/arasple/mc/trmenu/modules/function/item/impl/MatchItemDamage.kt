package me.arasple.mc.trmenu.modules.function.item.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.function.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:17
 */
@Suppress("DEPRECATION")
class MatchItemDamage : MatchItemIdentifier("(dat(a)?|damage)(s)?") {

    override fun match(player: Player, itemStack: ItemStack): Boolean = itemStack.durability == getDamage(player)

    override fun apply(player: Player, itemStack: ItemStack): ItemStack {
        itemStack.durability = getDamage(player)
        return itemStack
    }

    fun getDamage(player: Player) = NumberUtils.toShort(getContent(player), 0)

}