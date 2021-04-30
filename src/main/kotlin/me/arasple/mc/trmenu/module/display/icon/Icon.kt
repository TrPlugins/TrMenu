package me.arasple.mc.trmenu.module.display.icon

import me.arasple.mc.trmenu.api.menu.IIcon
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.service.Performance
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.collections.IndivList

/**
 * @author Arasple
 * @date 2021/1/25 10:41
 */
class Icon(
    val id: String,
    val refresh: Long,
    update: Array<Int>,
    val position: Position,
    val defIcon: IconProperty,
    val subs: IndivList<IconProperty>
) : IIcon {

    override fun startup(session: MenuSession) {
        update.forEach { (period, frames) ->
            session.arrange(
                Tasks.timer(10, period, true) {
                    onUpdate(session, frames)
                },
                true
            )
        }
        if (refresh > 0) {
            session.arrange(
                Tasks.timer(10, refresh, true) {
                    onRefresh(session)
                },
                true
            )
        }
    }

    override fun onUpdate(session: MenuSession, frames: Set<Int>) {
        Performance.check("Menu:Icon:Update") {
            val icon = getProperty(session)
            frames.forEach {
                when (it) {
                    // Position
                    3 -> {
                        val previous = position.currentPosition(session)
                        position.cycleIndex(session)
                        position.updatePosition(session)
                        val exclude = position.currentPosition(session).let { current ->
                            return@let previous.filter { pre -> !current.contains(pre) }
                        }
                        settingItem(session, icon)
                        session.receptacle?.setItem(null, exclude)
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
                        settingItem(session, icon)
                    }
                }
            }
        }
    }

    override fun settingItem(session: MenuSession, icon: IconProperty) {
        session.receptacle?.setItem(icon.display.get(session), position.currentPosition(session))
    }

    override fun onRefresh(session: MenuSession) {
        val iterator = subs.elements.withIndex().iterator()
        filter(session, iterator)
    }

    override fun onReset(session: MenuSession) {
        position.reset(session)
        val resetIcon: (IconProperty) -> Unit = {
            it.display.texture.reset(session.id)
            it.display.name.reset(session.id)
            it.display.lore.reset(session.id)
            it.display.cache.remove(session.id)
        }
        resetIcon(defIcon)
        subs.elements.forEach(resetIcon)
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
    internal val update: Map<Long, Set<Int>> = kotlin.run {
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
        return matcher(defIcon) || subs.elements.any { matcher(it) }
    }

    /**
     * 筛选子图标
     */
    private fun filter(session: MenuSession, iterator: Iterator<IndexedValue<IconProperty>>) {
        if (iterator.hasNext()) {
            val (index, property) = iterator.next()
            val eval = property.condition.eval(session)

            if (eval.asBoolean()) {
                subs[session.id] = index
                settingItem(session, getProperty(session))
            } else {
                subs[session.id] = -1
                filter(session, iterator)
            }
        } else {
            settingItem(session, getProperty(session))
        }
    }

}