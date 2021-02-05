package me.arasple.mc.trmenu.module.display.icon

import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
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
    val inherit: Boolean = false,
    val display: Item,
    val action: Map<Set<ClickType>, Reactions>
) {

    fun isTextureUpdatable(): Boolean {
        return display.meta.isDynamic || display.texture.cyclable() || display.texture.elements.any { it.dynamic }
    }

    fun isNameUpdatable(): Boolean {
        return display.name.cyclable() || display.name.elements.any { Regexs.containsPlaceholder(it) }
    }

    fun isLoreUpdatable(): Boolean {
        return display.lore.cyclable() || display.lore.elements.any { Regexs.containsPlaceholder(it.lore.joinToString(" ")) }
    }

    fun handleClick(type: ClickType, session: MenuSession) {
        val reactions = action.entries
            .filter { it.key.any { type -> type == ClickType.ALL || type==ClickType.NUMBER_KEY && type.isNumberKeyClick()} || it.key.contains(type) }
            .map { it.value }

        reactions.forEach { it.eval(session) }
    }


}