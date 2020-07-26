package me.arasple.mc.trmenu.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.izzel.taboolib.Version
import io.izzel.taboolib.loader.internal.IO
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
import joptsimple.internal.Strings
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/7/6 20:44
 */
object Skulls {

    val apis = arrayOf(
        arrayOf("https://api.minetools.eu/uuid/", "https://api.minetools.eu/profile/"),
        arrayOf("https://api.mojang.com/users/profiles/minecraft/", "https://sessionserver.mojang.com/session/minecraft/profile/")
    )

    val cache = mutableMapOf<String, ItemStack>()
    val cachePlayerTexture = mutableMapOf<String, String?>()

    fun getPlayerHead(name: String): ItemStack = cache.computeIfAbsent(name) {
        val texture = getPlayerTexture(name) ?: kotlin.run {
            val head = ItemBuilder(Materials.PLAYER_HEAD.parseItem()).build()
            getPlayerTexture(name) {
                setTextureSkull(it, head)
            }
            return@computeIfAbsent head
        }
        return@computeIfAbsent getTextureSkull(texture)
    }

    private fun getPlayerTexture(id: String) = getPlayerTexture(id, null)

    private fun getPlayerTexture(id: String, consumer: Consumer<String>?): String? {
        if (cachePlayerTexture.containsKey(id)) {
            return cachePlayerTexture[id]
        } else {
            val player = Bukkit.getPlayerExact(id)
            if (player != null && Version.isAfter(Version.v1_13)) {
                val nms = PacketsHandler.getPlayerTexture(player)
                if (!Strings.isNullOrEmpty(nms)) {
                    cachePlayerTexture[id] = nms
                    return cachePlayerTexture[id]
                }
            }
            cachePlayerTexture[id] = null
            Tasks.run(true) {
                try {
                    // https://api.minetools.eu/profile/%uuid%
                    val mojang = TrMenu.SETTINGS.getBoolean("Options.Skull-Mojang-API", false)
                    val api = if (mojang) apis[1] else apis[0]
                    val userProfile = JsonParser().parse(IO.readFromURL("${api[0]}$id")) as JsonObject
                    val uuid = userProfile["id"].asString
                    val textures = (JsonParser().parse(IO.readFromURL("${api[1]}$uuid")) as JsonObject).let {
                        if (mojang) return@let it.getAsJsonArray("properties")
                        else return@let it.getAsJsonObject("raw").getAsJsonArray("properties")
                    }
                    for (element in textures) if ("textures" == element.asJsonObject["name"].asString) cachePlayerTexture[id] = element.asJsonObject["value"].asString
                    if (consumer != null) cachePlayerTexture[id]?.let { consumer.accept(it) }
                } catch (e: Throwable) {
                    Msger.printErrors("PLAYER-HEAD", e)
                }
            }
        }
        return cachePlayerTexture[id]
    }

    fun getTextureSkull(texture: String) = setTextureSkull(texture, Materials.PLAYER_HEAD.parseItem()!!)

    fun setTextureSkull(texture: String, item: ItemStack): ItemStack = cache.computeIfAbsent(texture) {
        val meta = item.itemMeta as SkullMeta
        val profile = GameProfile(UUID.randomUUID(), null)
        val field = meta.javaClass.getDeclaredField("profile")
        profile.properties.put("textures", Property("textures", texture, "TrMenu_TexturedSkull"))
        field.isAccessible = true
        field[meta] = profile
        item.itemMeta = meta
        return@computeIfAbsent item
    }

}
