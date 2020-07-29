package me.arasple.mc.trmenu.configuration

import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.utils.Utils
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/7/21 8:30
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        test()
    }

    fun test() {
        ///trmenu action Arasple json: Hello World! <ClickMe?command=spawn&hover=to spawn> <--- Click That
//        val text = "Hello World! <ClickMe?command=spawn&hover=to spawn> <--- Click That".replace('?', '&')
        val text = "&3Hello, &b{player_name}&3, Buy Ranks on our <&2&nstore?url=https://store.example.net> &3site.".replace('?', '&')
        if (Utils.isJson(text)) {
            println("is json")
            return
        }
        Variables(text).find().variableList.forEach { part ->
            if (part.text == text) {
                return
            } else if (!part.isVariable) println("Append:" + part.text)
            else {
                val args = part.text.split('&', limit = 2).toMutableList().also {
                    println("Append:" + it[0])
                    it.removeAt(0)
                }
                args.firstOrNull()?.split('&')?.forEach { it ->
                    it.split('&').forEach {
                        val event = it.split(':', '=', limit = 2)
                        if (event.size >= 2) event[1].let { value ->
                            when (event[0].toLowerCase()) {
                                "hover" -> println("Hover:$value")
                                "suggest" -> println("Suggest:$value")
                                "command" -> println("Command:$value")
                                "url" -> println("Url:$value")
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }
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