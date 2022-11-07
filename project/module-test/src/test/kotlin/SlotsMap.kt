/**
 * @author Arasple
 * @since 2022/11/6 14:21
 */


fun main() {

    /**
     * Pos=6
     * Expected=6,7,8 // 15,16,17
     * ######***
     * ######***
     * #########
     *
     * Pos=7
     * Expected=7,8 // 16,17
     * #######**(*)
     * #######**(*)
     * #########
     *
     * Pos=9 (baseline=1, baseindex = 0)
     * Expected=9,10,11 //  18,19,20
     * #########
     * ***######
     * ***######
     * #########
     *
     * Pos=11 (baseline=1, baseindex = 3)
     * Expected=11, 12, 13 //  20,21,22
     *
     * #########
     * ##***####
     * ##***####
     * #########
     */

    val windowWidth = 9
    val scale = 3 to 2
    val pos = 11

    val slotsMap by lazy {
        val result = mutableMapOf<Int, Int>()
        var counter = 0
        var baseLine = 0
        var baseIndex = pos
        while (baseIndex >= windowWidth) {
            baseIndex -= windowWidth
            baseLine++
        }

        for (x in baseLine until baseLine + scale.second) {
            for (y in baseIndex until baseIndex + scale.first) {
                result[counter++] = if (y >= windowWidth) -1 else windowWidth * x + y
            }
        }

        result
    }

    println(slotsMap.keys)
    println(slotsMap.values)

}