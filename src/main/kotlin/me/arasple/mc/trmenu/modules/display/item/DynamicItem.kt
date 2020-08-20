package me.arasple.mc.trmenu.modules.display.item

import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.item.property.Lore
import me.arasple.mc.trmenu.modules.display.item.property.Mat
import me.arasple.mc.trmenu.modules.display.item.property.Meta
import me.arasple.mc.trmenu.modules.display.item.property.Temp
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/5/30 14:08
 */
class DynamicItem(var material: Animated<Mat>, val meta: Meta, val cache: MutableMap<UUID, Temp>) {

    fun getItem(player: Player) = material.currentElement(player)

    fun nextItem(player: Player) = material.nextIndex(player)

    fun cache(player: Player) = cache.computeIfAbsent(player.uniqueId) { Temp(null, null) }

    fun displayName(player: Player, name: String?) {
        cache(player).name = Item.colorizeName(Msger.replace(player, name))
    }

    fun displayLore(player: Player, lore: Lore?) {
        cache(player).lore = lore?.formatedLore(player)
    }

    fun releaseItem(player: Player): ItemStack? {
        val itemStack = getItem(player)?.createItem(player)?.clone() ?: return null
        val itemMeta = itemStack.itemMeta

        if (meta.hasAmount()) {
            itemStack.amount = meta.amount(player)
        }
        if (itemMeta != null) {
            val display = cache(player)
            meta.shiny(player, itemMeta)
            meta.flags.forEach { itemMeta.addItemFlags(it) }
            display.name?.let { itemMeta.setDisplayName(it) }
            display.lore?.let { itemMeta.lore = it }
        }
        itemStack.itemMeta = itemMeta
        meta.nbt(player, itemStack)?.also { itemStack.itemMeta = it }
        return itemStack
    }

}