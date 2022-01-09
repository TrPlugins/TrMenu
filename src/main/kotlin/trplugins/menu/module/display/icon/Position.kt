package trplugins.menu.module.display.icon

import trplugins.menu.module.display.MenuSession
import trplugins.menu.util.Regexs
import trplugins.menu.util.collections.CycleList

/**
 * @author Arasple
 * @date 2021/1/26 12:42
 */
class Position(private val position: Map<Int, CycleList<Slot>>) {

    val enabledPages: Set<Int>
        get() {
            return position.keys
        }

    private val cache = mutableMapOf<Int, List<Int>>()

    fun currentPosition(session: MenuSession): List<Int> {
        return cache.computeIfAbsent(session.id) { refreshCurrentPosition(session) }
    }

    private fun refreshCurrentPosition(session: MenuSession): List<Int> {
        return position[session.page]?.current(session.id)!!.handler(session)
    }

    fun updatePosition(session: MenuSession) {
        cache[session.id] = refreshCurrentPosition(session)
    }

    fun cycleIndex(session: MenuSession) {
        position[session.page]?.cycleIndex(session.id)
    }

    fun reset(session: MenuSession) {
        cache.remove(session.id)
    }

    override fun toString(): String {
        return position.toString()
    }

    fun isUpdatable(): Boolean {
        return position.any { it -> it.value.cyclable() || it.value.elements.any { it.dynamic.isNotEmpty() } }
    }

    class Slot(private val static: Set<Int>, val dynamic: Set<String>) {

        fun handler(session: MenuSession): List<Int> {
            return static.plus(dynamic.map { session.parse(it).toIntOrNull() }).filterNotNull()
        }

        override fun toString(): String {
            return static.joinToString(", ", "[") + dynamic.joinToString(", ", postfix = "]")
        }

        companion object {

            fun readStaticSlots(string: String): List<Int> {
                val static = mutableListOf<Int>()
                if (string.contains("-")) {
                    val range = string.split("-", limit = 2)
                    val from = range[0].toIntOrNull()
                    val to = range[1].toIntOrNull()
                    if (from != null && to != null) static.addAll(from..to)
                } else {
                    string.toIntOrNull()?.also { static.add(it) }
                }
                return static
            }

            fun from(slots: Collection<Any>): Slot {
                val dynamic = mutableSetOf<String>()
                val static = mutableSetOf<Int>()
                slots.forEach { any ->
                    val it = any.toString()
                    if (Regexs.containsPlaceholder(it)) dynamic.add(it)
                    else static.addAll(readStaticSlots(it))
                }
                return Slot(static, dynamic)
            }

        }

    }

}