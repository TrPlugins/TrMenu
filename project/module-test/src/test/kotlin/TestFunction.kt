import cc.trixey.mc.trmenu.invero.module.TypeAddress
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/11/5 14:41
 */

val size = 4 to 3
val distributeMark = 2
val claimedSlots by lazy {
    val results = CopyOnWriteArrayList<Int>()
    var initial = distributeMark

    /*
    002000000
    000000000
    000000000
    000000000
    000000000
     */

    /*
    00XXXX000
    00XXXX000
    00XXXX000
    000000000
    000000000
     */

    /*
    expected:

    02 03 04 05
    11 12 13 14
    20 21 22 23
     */

    for (line in 0 until size.second) {
        for (row in 0 until size.first) {
            results += initial
            initial++
        }
        initial += (TypeAddress.GENERIC_9X6.width - size.first)
    }

    results
}

val slotsMap by lazy {
    // Relative: Absolute
    val result = HashMap<Int, Int>()
    var initial = distributeMark
    val (width, height) = size

    for (line in 0 until height) {
        for (row in 0 until width) {
            result[line * height + line + row] = initial
            initial++
        }
        initial += (TypeAddress.GENERIC_9X6.width - size.first)
    }
    result
}

fun main() {
    println(claimedSlots)
    println(slotsMap)
}