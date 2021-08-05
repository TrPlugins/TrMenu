package me.arasple.mc.trmenu.module.display.icon

import me.arasple.mc.trmenu.api.action.pack.Reactions
import taboolib.module.ui.receptacle.ReceptacleClickType
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.display.item.Item
import me.arasple.mc.trmenu.module.internal.script.Condition
import me.arasple.mc.trmenu.util.Regexs

/**
 * @author Arasple
 * @date 2021/1/25 10:53
 */
class IconProperty(
    val priority: Int,
    val condition: Condition,
    val display: Item,
    val action: Map<Set<ReceptacleClickType>, Reactions>
) {

    fun isTextureUpdatable(): Boolean {
        return display.meta.isDynamic || display.texture.cyclable() || display.texture.elements.any { it.dynamic }
    }

    fun isNameUpdatable(): Boolean {
        return display.name.cyclable() || display.name.elements.any { Regexs.containsPlaceholder(it) }
    }

    fun isLoreUpdatable(): Boolean {
        return display.lore.cyclable() || display.lore.elements.any { it -> Regexs.containsPlaceholder(it.lore.joinToString(" ") { it.first }) }
    }

    fun handleClick(type: ReceptacleClickType, session: MenuSession) {
        val reactions = action.entries
            .filter { set ->
                set.key.any { it == ReceptacleClickType.ALL || it == ReceptacleClickType.NUMBER_KEY && it.isNumberKeyClick() } || set.key.contains(
                    type
                )
            }
            .map { it.value }

        reactions.forEach { it.eval(session) }
    }


}