package me.arasple.mc.trmenu.test

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.display.Menu
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/8/2 9:55
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        val menu = loadMenu()
    }

    private fun loadMenu(): Menu {
        val inputStream = javaClass.getResourceAsStream("/Test.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))
        return MenuSerializer.loadMenu("Test", configuration)!!
    }

}