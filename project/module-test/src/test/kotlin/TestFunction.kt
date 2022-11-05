/**
 * @author Arasple
 * @since 2022/11/5 14:41
 */

val slotsMap by lazy {
    val windowWidth = 9
    val result = mutableMapOf<Int, Int>()
    val initial = 0
    val (width, height) = 3 to 3
    var relative = 0

    for (line in 0 until height) {
        for (row in 0 until width) {
            val absolute = line * windowWidth + initial + row
            result[relative] = absolute
            relative++
        }
    }

    result
}

fun main() {
    println(slotsMap.keys)
    println(slotsMap.values)
}