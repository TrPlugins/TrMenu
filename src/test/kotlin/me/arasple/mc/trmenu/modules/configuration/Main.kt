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
//        println(HexColor.translate("一条测试的消息 &<FFFFFF>白色&7, &86666, &<000000>hhhh... &<256,0,0>还支持rgb"))

        val inputStream = javaClass.getResourceAsStream("/Demo.yml")
        val configuration = MenuConfiguration()
        configuration.load(InputStreamReader(inputStream))

        val start = System.currentTimeMillis()

        val menu = MenuSerializer.loadMenu("Example", configuration)
        val icon = menu.icons.first().defIcon

        println("--------------------------------------------------\n\n")

        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")

    }

}