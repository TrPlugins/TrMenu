package me.arasple.mc.trmenu.modules.configuration

import me.arasple.mc.trmenu.modules.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.modules.configuration.serialize.MenuSerializer
import java.io.InputStreamReader


/**
 * @author Arasple
 * @date 2020/6/27 20:30
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        testMenu()
    }

    private fun testMenu() {
        val inputStream = javaClass.getResourceAsStream("/Demo.yml")
        val configuration = MenuConfiguration()
        configuration.load(InputStreamReader(inputStream))

        val start = System.currentTimeMillis()

        val menu = MenuSerializer.loadMenu("Example", configuration)
        val icon = menu.icons.first().defIcon

        println("--------------------------------------------------\n\n")
        println(icon.display.position)
        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")
    }

}