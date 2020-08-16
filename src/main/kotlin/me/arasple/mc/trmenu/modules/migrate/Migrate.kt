package me.arasple.mc.trmenu.modules.migrate

import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2020/8/16 21:01
 */
abstract class Migrate {

    abstract fun depend(): String

    abstract fun migrate()

    companion object {

        val exact = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val keys = listOf('#', '-', '@', '|', '=', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')

        fun getExactDate(): String = exact.format(System.currentTimeMillis())

    }

}