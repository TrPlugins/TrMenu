package me.arasple.mc.trmenu.configuration

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.loader.internal.IO
import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/7/21 8:30
 */
object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        println(testMineTool())
    }

    private fun testMineTool(): String? {
        val start = System.currentTimeMillis()

        val id = "Arasple"
        val userProfile = JsonParser().parse(IO.readFromURL("https://api.minetools.eu/uuid/$id")) as JsonObject
        val uuid = userProfile["id"].asString
        val textures = (JsonParser().parse(IO.readFromURL("https://api.minetools.eu/profile/$uuid")) as JsonObject).getAsJsonObject("raw").getAsJsonArray("properties")
        for (element in textures) if ("textures" == element.asJsonObject["name"].asString){
            return element.asJsonObject["value"].asString
        }

        println("Took: ${System.currentTimeMillis() - start}ms")
        return null
    }

    private fun testMojang(): String? {

        val id = "Arasple"
        val userProfile = JsonParser().parse(IO.readFromURL("https://api.mojang.com/users/profiles/minecraft/$id")) as JsonObject
        val uuid = userProfile["id"].asString
        val textures = (JsonParser().parse(IO.readFromURL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid")) as JsonObject).getAsJsonArray("properties")
        for (element in textures) if ("textures" == element.asJsonObject["name"].asString) return element.asJsonObject["value"].asString
        return null
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