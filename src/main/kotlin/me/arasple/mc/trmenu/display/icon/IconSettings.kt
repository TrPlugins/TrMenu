package me.arasple.mc.trmenu.display.icon

import me.arasple.mc.trmenu.display.Icon

/**
 * @author Arasple
 * @date 2020/5/30 13:48
 */
class IconSettings(val refresh: Int, var update: Array<Int>) {

    init {
        val list = update.toMutableList()
        while (list.size < 4) list.add(update.maxOrNull() ?: -1)
        update = list.toTypedArray()
    }

    fun collectUpdatePeriods(): Map<Int, Set<Int>> {
        val result = mutableMapOf<Int, MutableSet<Int>>()
        for (i in update.indices) {
            if (update[i] > 0) {
                result.computeIfAbsent(update[i]) { mutableSetOf() }.add(i)
            }
        }

        return result
    }

    fun fixUpdate(icon: Icon) {
        val animatables = mutableSetOf<Int>().let {
            it.addAll(getAnimatbles(icon.defIcon.display))
            icon.subIcons.forEach { sub -> it.addAll(getAnimatbles(sub.display)) }
            return@let it
        }
        update.indices.forEach {
            if (update[it] > 0 && !animatables.contains(it)) {
                update[it] = -1
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getAnimatbles(display: IconDisplay): Set<Int> {
        buildSet {
            if (display.item.material.isUpdatable()) this.add(0)
            if (display.name.isUpdatable()) this.add(1)
            if (display.lore.animatable || display.lore.elements.any { it.isUpdateable() }) this.add(2)
            if (display.position.values
                    .any { it ->
                        it.animatable || it.elements.any { it.dynamicSlots.isNotEmpty() }
                    }
            ) {
                this.add(3)
            }

            return this
        }
    }

}