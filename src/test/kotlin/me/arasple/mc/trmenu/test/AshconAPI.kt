package me.arasple.mc.trmenu.test

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.loader.internal.IO

/**
 * @author Arasple
 * @date 2020/8/4 9:49
 */
object AshconAPI {

    val ASHCON_API = arrayOf("https://api.ashcon.app/mojang/v2/user/")
    val CACHED_PROFILES = mutableMapOf<String, JsonObject>()

    fun getProfile(name: String) = CACHED_PROFILES.computeIfAbsent(name) { JsonParser().parse(IO.readFromURL("${ASHCON_API[0]}$name")) as JsonObject }

    fun getSkinTexture(name: String): String = getProfile(name).getAsJsonObject("textures").getAsJsonObject("raw").get("value").asString

}