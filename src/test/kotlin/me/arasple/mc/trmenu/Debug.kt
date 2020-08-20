package me.arasple.mc.trmenu

/**
 * @author Arasple
 * @date 2020/8/20 12:24
 */
object Debug {

    @JvmStatic
    fun main(args: Array<String>) {
        val args = "Example 0 1 2 3".split(" ")
        val arguments = if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size)

        println(arguments.joinToString(", ", "[", "]"))
    }

}