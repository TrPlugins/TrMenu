package me.arasple.mc.trmenu.test

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.modules.script.Scripts
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/8/2 9:55
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        arrayOf(
//            "\"%checkitem_mat:{0},amt:{math_CEILING(\${input_amount})[precision:0]}%\" == \"yes\"",
//            "\${meta_player} == \"yes\"",
            "\${bStats.query_servers_&a_&7 servers}"
        ).forEach {
            println(Scripts.translate(it))
        }
    }

    private fun loadMenu(): Menu {
        val inputStream = javaClass.getResourceAsStream("/Test.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))
        return MenuSerializer.loadMenu("Test", configuration)!!
    }

}