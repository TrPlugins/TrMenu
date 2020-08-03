package me.arasple.mc.trmenu.modules.item

import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import me.arasple.mc.trmenu.modules.item.impl.MatchItemAmount
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 21:32
 * 通过一段字符串来配置，可识别物品是否匹配，支持多个
 *
 * ; - 分割多个物品 <Item>;<Item2>
 * , - 分割特征匹配内容
 * : - 特征值
 */
data class ItemIdentifier(val raw: String, val identifiers: MutableSet<Identifier>) {

    constructor(raw: String) : this(raw, mutableSetOf())

    fun isMatch(player: Player, itemStack: ItemStack) = identifiers.none { !it.match(player, itemStack) }

    fun hasItem(player: Player) = identifiers.all {
        Items.hasItem(
            player.inventory, { itemStack ->
                it.match(player, itemStack)
            },
            it.amount(player)
        )
    }

    fun amount(player: Player) = identifiers.sumBy { it.amount(player) }

    fun isInvalid(): Boolean = identifiers.isEmpty() || identifiers.none { it.characteristic.isNotEmpty() }

    data class Identifier(val characteristic: MutableSet<MatchItemIdentifier>) {

        constructor() : this(mutableSetOf())

        fun match(player: Player, itemStack: ItemStack): Boolean = characteristic.none { !it.match(player, itemStack) }

        fun amount(player: Player): Int {
            return characteristic.firstOrNull { it is MatchItemAmount }?.let {
                (it as MatchItemAmount).getAmount(player)
            } ?: 1
        }

        fun buildItem(player: Player): ItemStack? {
            val itemStack = ItemBuilder(Material.AIR).build()
            characteristic.forEach {
                it.apply(player, itemStack)
            }
            return itemStack
        }

    }

    override fun toString(): String = raw

}