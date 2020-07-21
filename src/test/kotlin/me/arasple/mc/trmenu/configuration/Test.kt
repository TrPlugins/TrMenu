package me.arasple.mc.trmenu.configuration

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/7/21 8:30
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        testMenu()
    }

    private fun testMenu() {
        val inputStream = javaClass.getResourceAsStream("/Demo.yml")
        val configuration = MenuConfiguration()
        configuration.load(InputStreamReader(inputStream))

        val start = System.currentTimeMillis()

        val menu = MenuSerializer.loadMenu("Example", configuration)!!

        println("--------------------------------------------------\n\n")
        menu.icons.forEach {
            val pos = it.defIcon.display.position
            if (pos.isEmpty()) {
                println(it.id)
            }
        }
        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")
    }

}