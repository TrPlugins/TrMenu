package me.arasple.mc.trmenu.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.izzel.taboolib.Version
import io.izzel.taboolib.loader.internal.IO
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
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

    val MOJANG_API = arrayOf("https://api.mojang.com/users/profiles/minecraft/", "https://sessionserver.mojang.com/session/minecraft/profile/")
    val ASHCON_API = arrayOf("https://api.ashcon.app/mojang/v2/user/")

    val USER_NAME = "[a-zA-Z0-9_]*".toRegex()
    val DEFAULT_HEAD = Materials.PLAYER_HEAD.parseItem()!!
    val CACHED_SKULLS = mutableMapOf<String, ItemStack>()
    val CACHED_PLAYER_TEXTURE = mutableMapOf<String, String?>()

    fun getPlayerHead(name: String): ItemStack = try {
        if (USER_NAME.matches(name)) CACHED_SKULLS.computeIfAbsent(name) {
            val texture = CACHED_PLAYER_TEXTURE[name] ?: kotlin.run {
                val head = ItemBuilder(DEFAULT_HEAD.clone()).build()
                getPlayerTexture(name) { setTextureSkull(it, head) }
                return@computeIfAbsent head
            }
            return@computeIfAbsent getTextureSkull(texture)
        } else DEFAULT_HEAD
    } catch (e: Throwable) {
        DEFAULT_HEAD
    }

    private fun getPlayerTexture(id: String) = getPlayerTexture(id, null)

    private fun getPlayerTexture(id: String, consumer: Consumer<String>?): String? {
        if (CACHED_PLAYER_TEXTURE.containsKey(id)) {
            return CACHED_PLAYER_TEXTURE[id]
        } else {
            val player = Bukkit.getPlayerExact(id)
            if (player != null && Version.isAfter(Version.v1_13)) {
                val nms = PacketsHandler.getPlayerTexture(player)
                if (!Strings.isBlank(nms)) {
                    CACHED_PLAYER_TEXTURE[id] = nms
                    return CACHED_PLAYER_TEXTURE[id]
                }
            }
            CACHED_PLAYER_TEXTURE[id] = null
            Tasks.task(true) {
                try {
                    val mojang = TrMenu.SETTINGS.getBoolean("Options.Skull-Mojang-API", false)
                    var texture: String? = null
                    if (mojang) {
                        val profile = JsonParser().parse(IO.readFromURL("${MOJANG_API[0]}$id")) as JsonObject
                        val uuid = profile["id"].asString
                        val textures = (JsonParser().parse(IO.readFromURL("${MOJANG_API[1]}$uuid")) as JsonObject).let {
                            return@let it.getAsJsonArray("properties")
                        }

                        for (element in textures) if ("textures" == element.asJsonObject["name"].asString) {
                            texture = element.asJsonObject["value"].asString
                        }
                    } else {
                        val profile = JsonParser().parse(IO.readFromURL("${ASHCON_API[0]}$id")) as JsonObject
                        texture = profile.getAsJsonObject("textures").getAsJsonObject("raw").get("value").asString
                    }
                    CACHED_PLAYER_TEXTURE[id] = texture

                    if (consumer != null) {
                        CACHED_PLAYER_TEXTURE[id]?.let { consumer.accept(it) }
                    }
                } catch (e: Throwable) {
                    if (e.message != "JsonNull") Msger.printErrors("PLAYER-HEAD", e)
                }
            }
        }
        return CACHED_PLAYER_TEXTURE[id]
    }

    fun getTextureSkull(texture: String) = setTextureSkull(texture, DEFAULT_HEAD.clone())

    fun setTextureSkull(texture: String, item: ItemStack): ItemStack = CACHED_SKULLS.computeIfAbsent(texture) {
        val meta = item.itemMeta as SkullMeta
        val profile = GameProfile(UUID.randomUUID(), null)
        val field = meta.javaClass.getDeclaredField("profile")
        profile.properties.put(
            "textures",
            Property(
                "textures",
                if (texture.length in 60..100) {
                    Base64.getEncoder().encodeToString("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/$texture\"}}}".toByteArray())
                } else {
                    texture
                },
                "TrMenu_TexturedSkull"
            )
        )
        field.isAccessible = true
        field[meta] = profile
        item.itemMeta = meta
        return@computeIfAbsent item
    }

    fun getSkullTexture(skull: ItemStack): String? {
        val meta = skull.itemMeta ?: return null
        val field = meta.javaClass.getDeclaredField("profile").also { it.isAccessible = true }
        (field.get(meta) as GameProfile?)?.properties?.values()?.forEach {
            if (it.name == "textures")
                return it.value
        }
        return null
    }

}
