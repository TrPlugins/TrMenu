package me.arasple.mc.trmenu.modules.item.impl

import me.arasple.mc.trmenu.display.item.property.Mat
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

    override fun apply(player: Player, itemStack: ItemStack): ItemStack {
        itemStack.type = Mat.serializeItemBuilder(getContent(player)).build().type
        return itemStack
    }

}