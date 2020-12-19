package me.arasple.mc.trmenu.modules.service

import io.izzel.taboolib.kotlin.Tasks
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2020/12/6 10:50
 */
data class Mirror(var times: Long = 0, var total: Double = 0.0, var lowest: Double = 0.0, var highest: Double = 0.0) {

    companion object {

        val data = mutableMapOf<String, Mirror>()

        fun get(id: String): Mirror {
            return data.computeIfAbsent(id) { Mirror() }
        }

        fun eval(id: String, block: () -> Unit): Mirror {
            return get(id).eval(block)
        }

        fun async(id: String, block: () -> Unit): Mirror {
            return get(id).evalAsync(block)
        }

    }

    inline fun eval(block: () -> Unit): Mirror {
        val start = System.nanoTime()
        block()
        val last = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start).toDouble()
        process(last)
        return this
    }

    inline fun evalAsync(crossinline block: () -> Unit): Mirror {
        Tasks.task(true) {
            block()
        }
        return this
    }

    fun process(last: Double) {
        times++
        total += last
        if (last > highest) highest = last
        if (last < lowest) lowest = last
    }

    fun average(): Double {
        return total / times
    }

}
