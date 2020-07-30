package me.arasple.mc.trmenu.modules.item.base

import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/13 22:04
 */
abstract class MatchItemIdentifier(val name: Regex, private var content: String) {

    constructor(name: String) : this(Regex(name), "")

    abstract fun match(player: Player, itemStack: ItemStack): Boolean

    abstract fun apply(player: Player, itemStack: ItemStack): ItemStack

    fun getContent(player: Player): String = Msger.replace(player, content)

    fun getContent() = content

    fun setContent(content: String) {
        this.content = content
    }

    fun newInstance(): MatchItemIdentifier = javaClass.getDeclaredConstructor().newInstance()

}