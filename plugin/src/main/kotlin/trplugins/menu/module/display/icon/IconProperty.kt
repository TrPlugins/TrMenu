package trplugins.menu.module.display.icon

import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.api.receptacle.ReceptacleClickType
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.display.item.Item
import trplugins.menu.util.Regexs

/**
 * @author Arasple
 * @date 2021/1/25 10:53
 */
class IconProperty(
    val priority: Int,
    val condition: String,
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

        reactions.forEach { it.eval(adaptPlayer(session.viewer)) }
    }


}