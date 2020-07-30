package me.arasple.mc.trmenu.modules.item.impl

import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import me.arasple.mc.trmenu.utils.Skulls
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
 * @author Arasple
 * @date 2020/7/29 20:04
 */
class MatchItemTexture : MatchItemIdentifier("(skull|head)?(-)?texture(s)?") {

    override fun match(player: Player, itemStack: ItemStack): Boolean {
        val itemMeta = itemStack.itemMeta ?: return false
        if (itemMeta is SkullMeta) {
            return Skulls.getSkullTexture(itemStack) == getContent()
        }
        return false
    }

    override fun apply(player: Player, itemStack: ItemStack) = Skulls.getTextureSkull(getContent(player))

}