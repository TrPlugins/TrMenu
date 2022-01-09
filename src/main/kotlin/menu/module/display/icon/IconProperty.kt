package menu.module.display.icon

import trmenu.api.reaction.Reactions
import taboolib.module.ui.receptacle.ReceptacleClickType
import trmenu.api.action.eval
import trmenu.module.display.MenuSession
import trmenu.module.display.item.Item
import trmenu.util.Regexs

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

        reactions.forEach { it.eval(session) }
    }


}