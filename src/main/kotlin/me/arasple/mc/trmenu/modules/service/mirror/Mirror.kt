package me.arasple.mc.trmenu.modules.service.mirror

/**
 * @author Bkm016
 * @date 2018-12-24 16:32
 */
object Mirror {

    val dataMap = mutableMapOf<String, MirrorData>()

    fun get(id: String, total: Boolean = true): MirrorData {
        return dataMap.computeIfAbsent(id) { MirrorData(total) }
    }

    fun eval(id: String, total: Boolean = true, runnable: Runnable): MirrorData {
        return dataMap.computeIfAbsent(id) { MirrorData(total) }.eval(runnable)
    }

    fun async(id: String, total: Boolean = true, runnable: Runnable) {
        dataMap.computeIfAbsent(id) { MirrorData(total) }.evalAsync(runnable)
    }

}