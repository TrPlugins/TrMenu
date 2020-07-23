package me.arasple.mc.trmenu.configuration

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.utils.Nodes
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/7/21 8:30
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        Nodes.read("<TITLE=&c&l权限不足><SUBTITLE=&7&l打开本菜单需要权限 &ctrmenu.use><FADEIN=20><STAY=20><FADEOUT=20>").second.forEach {
            println("Key: ${it.key.name}. Value: ${it.value}")
        }
    }

    private fun testMenu() {
        val inputStream = javaClass.getResourceAsStream("/Demo.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))

        val start = System.currentTimeMillis()

        val menu = MenuSerializer.loadMenu("Example", configuration)!!

        println("--------------------------------------------------\n\n")
        menu.icons.forEach {
            val pos = it.defIcon.display.position
            println(it.id + ". Update: ${it.settings.update.joinToString(",")}  Pos: $pos")
        }
        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")
    }

}