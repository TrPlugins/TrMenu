package me.arasple.mc.trmenu.test

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.loader.internal.IO
import me.arasple.mc.trmenu.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.configuration.serialize.MenuSerializer
import me.arasple.mc.trmenu.display.Menu
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/8/2 9:55
 */
object Test {

    val MOJANG_API = arrayOf("https://api.mojang.com/users/profiles/minecraft/", "https://sessionserver.mojang.com/session/minecraft/profile/")

    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
//        println(AshconAPI.getSkinTexture("Arasple"))

        arrayOf(
            "Arasple"
        ).forEach { id ->
            val profile = JsonParser().parse(IO.readFromURL("${MOJANG_API[0]}$id")) as JsonObject
            val uuid = profile["id"].asString
            val textures = (JsonParser().parse(IO.readFromURL("${MOJANG_API[1]}$uuid")) as JsonObject).let {
                return@let it.getAsJsonArray("properties")
            }

            for (element in textures) if ("textures" == element.asJsonObject["name"].asString) {
                println(element.asJsonObject["value"].asString)
            }
        }

        println("Took: ${System.currentTimeMillis() - start}")
    }

    private fun loadMenu(): Menu {
        val inputStream = javaClass.getResourceAsStream("/Test.yml")
        val configuration = MenuConfiguration("")
        configuration.load(InputStreamReader(inputStream))
        return MenuSerializer.loadMenu("Test", configuration)!!
    }

}