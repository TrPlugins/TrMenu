package me.arasple.mc.trmenu.modules.service.mirror

import me.arasple.mc.trmenu.util.Tasks
import java.util.concurrent.TimeUnit

/**
 * @author Bkm016
 * @date 2018-12-24 16:32
 */
data class MirrorData(val total: Boolean, var timeTotal: Double = 0.0, var timeLatest: Double = 0.0, var times: Long = 0, var lowest: Double = 0.0, var highest: Double = 0.0, private var runtime: Long = 0) {

    fun start(): MirrorData {
        runtime = System.nanoTime()
        return this
    }

    fun stop(): MirrorData {
        timeLatest = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - runtime).toDouble()
        timeTotal += timeLatest
        if (total) {
            times += 1
        }
        if (timeLatest > highest) highest = timeLatest
        if (timeLatest < lowest) lowest = timeLatest
        return this
    }

    fun reset(): MirrorData {
        timeTotal = 0.0
        timeLatest = 0.0
        times = 0
        return this
    }

    fun eval(runnable: Runnable): MirrorData {
        start()
        runnable.run()
        stop()
        return this
    }

    fun evalAsync(runnable: Runnable) {
        Tasks.task(true) {
            eval(runnable)
        }
    }

    fun average(): Double {
        return timeTotal / times
    }

}