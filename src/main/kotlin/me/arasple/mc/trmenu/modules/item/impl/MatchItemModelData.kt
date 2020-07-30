package me.arasple.mc.trmenu.modules.item.impl

import io.izzel.taboolib.Version
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:17
 */
class MatchItemModelData : MatchItemIdentifier("") {

    override fun match(player: Player, itemStack: ItemStack): Boolean = Version.isAfter(Version.v1_14) && itemStack.itemMeta?.customModelData == NumberUtils.toInt(getContent(player), 0)

    override fun apply(player: Player, itemStack: ItemStack): ItemStack {
        val itemMeta = itemStack.itemMeta ?: return itemStack
        itemMeta.setCustomModelData(NumberUtils.toInt(getContent(player), 0))
        itemStack.itemMeta = itemMeta
        return itemStack
    }


}