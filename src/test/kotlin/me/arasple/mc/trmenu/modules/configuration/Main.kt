package me.arasple.mc.trmenu.modules.configuration

import me.arasple.mc.trmenu.modules.configuration.menu.MenuConfiguration
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/6/27 20:30
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val inputStream = javaClass.getResourceAsStream("/Demo.yml")
        val menu = MenuConfiguration()
        menu.load(InputStreamReader(inputStream))

        val start = System.currentTimeMillis()

        println("--------------------------------------------------\n\n")

        println("Options: ${menu.getOptionDefaultArguments()}, ${menu.getOptionHidePlayerInventory()}")

        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")

    }

}