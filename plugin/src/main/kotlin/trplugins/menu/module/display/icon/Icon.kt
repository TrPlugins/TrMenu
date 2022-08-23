package trplugins.menu.module.display.icon

import trplugins.menu.api.menu.IIcon
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.service.Performance
import trplugins.menu.util.collections.IndivList
import taboolib.common.platform.function.submit
import trplugins.menu.module.internal.script.evalScript

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
                submit(delay = 10, period = period, async = true) {
                    onUpdate(session, frames)
                },
                true
            )
        }
        if (refresh > 0) {
            session.arrange(
                submit(delay = 10, period = refresh, async = true) {
                    onRefresh(session)
                },
                true
            )
        }
    }

    override fun onUpdate(session: MenuSession, frames: Set<Int>) {
        Performance.check("Menu:Icon:Update") {
            val menuId = session.menu?.id // 缓存菜单id避免打开下一个菜单出现图标覆盖
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
                        settingItem(session, icon, menuId)
                        session.receptacle?.setElement(null, exclude)
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
                        settingItem(session, icon, menuId)
                    }
                }
            }
        }
    }

    override fun settingItem(session: MenuSession, icon: IconProperty, lastMenuId: String?) {
        if (!isAvailable(session) || (lastMenuId != null && session.menu?.id != lastMenuId)) {
            return
        }
        session.receptacle?.setElement(icon.display.get(session), position.currentPosition(session))
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
        return if (!subs.isEmpty() && subs.getIndex(session.id) >= 0) subs[session.id] ?: defIcon else defIcon
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
        val menuId = session.menu?.id // 缓存菜单id避免打开下一个菜单出现图标覆盖
        if (iterator.hasNext()) {
            val (index, property) = iterator.next()
            val eval = property.condition.evalScript(session)

            if (eval.asBoolean()) {
                subs[session.id] = index
                settingItem(session, getProperty(session), menuId)
            } else {
                subs[session.id] = -1
                filter(session, iterator)
            }
        } else {
            settingItem(session, getProperty(session), menuId)
        }
    }

}