package me.arasple.mc.trmenu.util.bukkit

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import me.arasple.mc.trmenu.api.receptacle.nms.NMS
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.common.platform.submit
import taboolib.library.xseries.XMaterial
import java.net.URL
import java.util.*

/**
 * @author Arasple
 * @date 2021/1/27 14:05
 */
object Heads {

    private val MOJANG_API = arrayOf(
        "https://api.mojang.com/users/profiles/minecraft/",
        "https://sessionserver.mojang.com/session/minecraft/profile/"
    )

    private val DEFAULT_HEAD = XMaterial.PLAYER_HEAD.parseItem()!!
    private val CACHED_PLAYER_TEXTURE = mutableMapOf<String, String?>()
    private val CACHED_SKULLS = mutableMapOf<String, ItemStack>()

    fun cacheSize(): Pair<Int, Int> {
        return CACHED_SKULLS.size to CACHED_PLAYER_TEXTURE.size
    }

    fun getHead(id: String): ItemStack {
        return if (id.length > 20) getCustomTextureHead(id)
        else getPlayerHead(id)
    }

    fun getPlayerHead(name: String): ItemStack {
        return CACHED_SKULLS.computeIfAbsent(name) {
            DEFAULT_HEAD.clone().also { item -> playerTexture(name) { modifyTexture(it, item) } }
        }
    }

    fun getCustomTextureHead(texture: String): ItemStack {
        return CACHED_SKULLS.computeIfAbsent(texture) {
            modifyTexture(texture, DEFAULT_HEAD.clone())
        }
    }

    fun seekTexture(itemStack: ItemStack): String? {
        val meta = itemStack.itemMeta ?: return null

        if (meta is SkullMeta) {
            meta.owningPlayer?.name?.let { return it }
        }

        val field = meta.javaClass.getDeclaredField("profile").also { it.isAccessible = true }
        (field.get(meta) as GameProfile?)?.properties?.values()?.forEach {
            if (it.name == "textures") return it.value
        }
        return null
    }

    /**
     * PRIVATE UTILS
     */
    private fun playerTexture(name: String, block: (String) -> Unit) {
        when {
            HookPlugin.getSkinsRestorer().isHooked -> {
                HookPlugin.getSkinsRestorer().getPlayerSkinTexture(name)?.also(block)
            }
            Bukkit.getPlayer(name)?.isOnline == true -> {
                NMS.INSTANCE.getGameProfile(Bukkit.getPlayer(name)!!).properties["textures"]
                    .find { it.value != null }?.value
                    ?.also(block)
            }
            else -> {
                submit(async = true) {
                    val profile = JsonParser().parse(fromURL("${MOJANG_API[0]}$name")) as JsonObject
                    val uuid = profile["id"].asString
                    (JsonParser().parse(fromURL("${MOJANG_API[1]}$uuid")) as JsonObject).getAsJsonArray("properties")
                    (JsonParser().parse(fromURL("${MOJANG_API[1]}$uuid")) as JsonObject).getAsJsonArray("properties")
                        .forEach {
                            if ("textures" == it.asJsonObject["name"].asString) {
                                CACHED_PLAYER_TEXTURE[name] = it.asJsonObject["value"].asString.also(block)
                            }
                        }
                }
            }
        }
    }

    private fun modifyTexture(input: String, itemStack: ItemStack): ItemStack {
        val meta = itemStack.itemMeta as SkullMeta
        val profile = GameProfile(UUID.randomUUID(), null)
        val field = meta.javaClass.getDeclaredField("profile")
        val texture = if (input.length in 60..100) encodeTexture(input) else input

        profile.properties.put("textures", Property("textures", texture, "TrMenu_TexturedSkull"))
        field.isAccessible = true
        field[meta] = profile
        itemStack.itemMeta = meta
        return itemStack
    }

    private fun encodeTexture(input: String): String {
        val encoder = Base64.getEncoder()
        return encoder.encodeToString("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/$input\"}}}".toByteArray())
    }

    private fun fromURL(url: String): String {
        return try {
            String(URL(url).openStream().readBytes())
        } catch (t: Throwable) {
            ""
        }
    }


}