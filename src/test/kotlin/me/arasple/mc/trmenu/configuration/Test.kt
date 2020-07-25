package me.arasple.mc.trmenu.configuration

import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
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
        val inputStream = javaClass.getResourceAsStream("/ExampleX.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))

        val read = configuration.get("Icons.S.actions.all")

//        val start = System.currentTimeMillis()
//
//        val menu = MenuSerializer.loadMenu("Example", configuration)!!
//
//        println("--------------------------------------------------\n\n")
//        menu.icons.forEach {
//            val handlers = it.defIcon.clickHandler.handlers
//            handlers.forEach {
////                println(it)
//            }
//        }
//        println("\n\n--------------------------------------------------")
//        println("[END - Took: ${System.currentTimeMillis() - start} ms]")
//        println("--------------------------------------------------")
    }

}