package me.arasple.mc.trmenu.module.display.icon

import me.arasple.mc.trmenu.api.menu.IIcon
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.collections.IndivList

/**
 * @author Arasple
 * @date 2021/1/25 10:41
 */
class Icon(
    val id: String,
    private val refresh: Long,
    update: Array<Int>,
    val position: Position,
    private val defIcon: IconProperty,
    private val subs: IndivList<IconProperty>
) : IIcon {

    override fun startup(session: MenuSession) {
        update.forEach { (period, frames) ->
            session.arrange(
                Tasks.timer(period, period, true) {
                    onUpdate(session, frames)
                },
                true
            )
        }
        if (refresh > 0) {
            session.arrange(
                Tasks.timer(refresh, refresh, true) {
                    onRefresh(session)
                },
                true
            )
        }
    }

    override fun onUpdate(session: MenuSession, frames: Set<Int>) {
        val icon = getProperty(session)
        frames.forEach {
            when (it) {
                // Position
                3 -> {
                    position.cycleIndex(session)
                    position.updatePosition(session)
                    session.updateActiveSlots()
                }
                // Texture, Name, Lore
                else -> {
                    val display = icon.display
                    when (it) {
                        0 -> display.updateTexture(session)
                        1 -> display.updateName(session)
                        2 -> display.updateLore(session)
                        else -> {
                        }
                    }
                }
            }
        }
        settingItem(session, icon)
    }

    override fun settingItem(session: MenuSession, icon: IconProperty) {
        session.receptacle?.setItem(icon.display.get(session), position.currentPosition(session))
    }

    override fun onRefresh(session: MenuSession) {
        val iterator = subs.elements.withIndex().iterator()
        filter(session, iterator)
    }

    override fun getProperty(session: MenuSession): IconProperty {
        return if (!subs.isEmpty() && subs.getIndex(session.id) >= 0) subs[session.id]!! else defIcon
    }

    override fun isAvailable(session: MenuSession): Boolean {
        return position.enabledPages.contains(session.page)
    }

    /**
     * 更新周期
     */
    private val update: Map<Long, Set<Int>> = kotlin.run {
        val result = mutableMapOf<Long, MutableSet<Int>>()
        val fallback = update.maxOrNull() ?: -1
        val array = Array(4) { update.getOrElse(it) { fallback } }

        array.forEachIndexed { index, i ->
            if (index <= 4 && i > 0) {
                val allow = when (index) {
                    0 -> match { it.isTextureUpdatable() }
                    1 -> match { it.isNameUpdatable() }
                    2 -> match { it.isLoreUpdatable() }
                    3 -> match { position.isUpdatable() }
                    else -> false
                }
                if (!allow) array[index] = -1
            }
        }
        array.indices.forEach {
            if (array[it] > 0) result.computeIfAbsent(array[it].toLong()) { mutableSetOf() }.add(it)
        }
        result
    }

    private fun match(matcher: (IconProperty) -> Boolean): Boolean {
        return matcher(defIcon) && subs.elements.all { matcher(it) }
    }

    /**
     * 筛选子图标
     */
    private fun filter(session: MenuSession, iterator: Iterator<IndexedValue<IconProperty>>) {
        if (iterator.hasNext()) {
            val (index, property) = iterator.next()
            val eval = property.condition.eval(session)

            if (eval.asBoolean()) {
                subs.setIndex(session.id, index)
                settingItem(session, getProperty(session))
            } else {
                subs.setIndex(session.id, -1)
                filter(session, iterator)
            }
        } else {
            settingItem(session, getProperty(session))
        }
    }

}