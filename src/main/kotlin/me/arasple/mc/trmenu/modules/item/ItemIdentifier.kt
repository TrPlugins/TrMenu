package me.arasple.mc.trmenu.modules.item

import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
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
data class ItemIdentifier(val raw: String, val identifiers: MutableList<Identifier>) {

    constructor(raw: String) : this(raw, mutableListOf())

    fun isMatch(player: Player, itemStack: ItemStack): Boolean = identifiers.none { !it.match(player, itemStack) }

    fun isInvalid(): Boolean = identifiers.isEmpty() || identifiers.none { it.characteristic.isNotEmpty() }

    data class Identifier(val characteristic: MutableList<MatchItemIdentifier>) {

        constructor() : this(mutableListOf())

        fun match(player: Player, itemStack: ItemStack): Boolean = characteristic.none { !it.match(player, itemStack) }

    }

    override fun toString(): String = raw

}