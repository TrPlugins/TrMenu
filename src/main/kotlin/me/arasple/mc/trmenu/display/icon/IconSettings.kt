package me.arasple.mc.trmenu.display.icon

/**
 * @author Arasple
 * @date 2020/5/30 13:48
 */
class IconSettings(val refresh: Int, val update: Array<Int>) {

    fun getUpdateMaterials(): Int = update[0]

    fun getUpdateNames(): Int = update[1]

    fun getUpdateLores(): Int = update[2]

    fun getUpdateSlots(): Int = update[3]

    fun collectUpdatePeriods(): Map<Int, Set<Int>> = mutableMapOf<Int, MutableSet<Int>>().let {
        for (i in update.indices) if (update[i] > 0) it.computeIfAbsent(update[i]) { mutableSetOf() }.add(i)
        return@let it
    }

}