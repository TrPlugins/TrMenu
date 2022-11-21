package unit

/**
 * @author Arasple
 * @since 2022/11/21 11:25
 */
val objects = mutableListOf<String>().apply {
    for (i in 0..16) {
        add(
            buildString { for (x in 0..8) append(i) }
        )
    }
}

var index: Int = 0
    set(value) {
        field = if (value > maxIndex) 0
        else if (value < 0) value + objects.size
        else value
    }

val maxIndex = objects.lastIndex

const val columViewSize = 6

/*
Expected       Index
[333333333]    -2 = 3
[444444444]
[000000000]

[444444444]    -1 = 4
[000000000]
[111111111]

[000000000]    0
[111111111]
[222222222]

[111111111]    1
[222222222]
[333333333]

[222222222]    2
[333333333]
[444444444]

[333333333]    3  (3)
[444444444]       (4)
[000000000]       (0)

[444444444]    4  (4)
[000000000]       (0)
[111111111]       (1)

LoppEnd
 */


/*
Horizontal Calculate

  Width = 7, Height = 4

  0123456
  0123456
  0123456
  0123456

[0] = 0, 7, 14, 21
[1] = 1, 8, 15, 22

line ORDER = 0~6
height SCALE.SECOND = 4
width SCALE.FIRST = 7
index it

order + it * width

 */

fun main() {
    for (i in -1..13) {
        index = i
        val apply = (index until index + columViewSize).map { if (it > maxIndex) it - objects.size else it }
        println("------------------------------")
        println("INDEX # $index")
        println("apply: $apply")
    }
}