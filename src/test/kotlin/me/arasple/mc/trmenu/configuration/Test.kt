package me.arasple.mc.trmenu.configuration

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.modules.expression.Expressions
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/7/21 8:30
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        println(
            Expressions.parseExpression("!hasMoney.\"{3}*{6}\"")
        )
    }

    private fun testMenu() {
        val inputStream = javaClass.getResourceAsStream("/ExampleX.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))
        val start = System.currentTimeMillis()
        val menu = MenuSerializer.loadMenu("Example", configuration)!!

        println("--------------------------------------------------\n\n")
        menu.icons.forEach {
            println("${it.id} : ${it.settings.collectUpdatePeriods()}")
        }
        println("\n\n--------------------------------------------------")
        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
        println("--------------------------------------------------")
    }

}