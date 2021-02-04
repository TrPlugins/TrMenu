package me.arasple.mc.trmenu.module.display.texture

import io.izzel.taboolib.Version
import io.izzel.taboolib.internal.xseries.XMaterial
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.api.menu.ITexture
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.module.internal.item.ItemSource
import me.arasple.mc.trmenu.util.Regexs
import me.arasple.mc.trmenu.util.bukkit.Heads
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta

/**
 * @author Arasple
 * @date 2021/1/24 11:50
 */
class Texture(
    val raw: String,
    val texture: String,
    val dynamic: Boolean,
    val static: ItemStack?,
    val type: TextureType,
    val meta: Map<TextureMeta, String>
) : ITexture {

    override fun generate(session: MenuSession): ItemStack {
        if (static != null) return static
        val temp = if (dynamic) session.parse(texture) else texture

        val itemStack = when (type) {
            TextureType.NORMAL -> parseMaterial(temp)
            TextureType.HEAD -> Heads.getHead(temp)
            TextureType.REPO -> ItemRepository.getItem(temp)
            TextureType.SOURCE -> ItemSource.fromSource(temp)
            TextureType.RAW -> Items.fromJson(temp)
        }

        if (itemStack != null) {
            val itemMeta = itemStack.itemMeta
            meta.forEach { (meta, metaValue) ->
                val value = session.parse(metaValue)
                when (meta) {
                    TextureMeta.DATA_VALUE -> {
                        @Suppress("DEPRECATION")
                        itemStack.durability = value.toShortOrNull() ?: 0
                    }
                    TextureMeta.MODEL_DATA -> {
                        itemMeta?.setCustomModelData(value.toInt()).also { itemStack.itemMeta = itemMeta }
                    }
                    TextureMeta.LEATHER_DYE -> if (itemMeta is LeatherArmorMeta) {
                        itemMeta.setColor(ItemHelper.serializeColor(value)).also { itemStack.itemMeta = itemMeta }
                    }
                    TextureMeta.BANNER_PATTERN -> itemStack // TODO
                }
            }
        }

        return itemStack ?: FALL_BACK
    }


    companion object {

        val FALL_BACK = ItemStack(Material.BEDROCK)

        fun createTexture(itemStack: ItemStack): String {
            val material = itemStack.type.name.toLowerCase().replace("_", " ")
            val itemMeta = itemStack.itemMeta

            // Head Meta
            if (itemMeta is SkullMeta) {
                val hdb =
                    if (HookPlugin.getHeadDatabase().isHooked) {
                        HookPlugin.getHeadDatabase().getId(itemStack)
                    } else ""

                return if (hdb != null) "source:HDB:$hdb"
                else "head:Heads.seekTexture(itemStack)"
            }
            // Model Data
            if (Version.isAfter(Version.v1_14) && itemMeta != null && itemMeta.hasCustomModelData()) {
                return "$material{model-data:${itemMeta.customModelData}}"
            }
            // Leather
            if (itemMeta is LeatherArmorMeta) {
                return "$material{dye:${ItemHelper.deserializeColor(itemMeta.color)}}"
            }
            // Banner

            return material
        }

        /**
         * Create a texture from string
         */
        fun createTexture(raw: String): Texture {
            var type = TextureType.NORMAL
            var static: ItemStack? = null
            var texture = raw
            val meta = mutableMapOf<TextureMeta, String>()

            TextureMeta.values().forEach {
                it.regex.find(raw)?.groupValues?.get(1)?.also { value ->
                    meta[it] = value
                    texture = texture.replace(it.regex, "")
                }
            }

            TextureType.values().filter { it.group != -1 }.forEach {
                it.regex.find(texture)?.groupValues?.get(it.group)?.also { value ->
                    type = it
                    texture = value
                }
            }

            val dynamic = Regexs.containsPlaceholder(texture)
            if (type == TextureType.NORMAL) {
                if (texture.startsWith("{")) {
                    val json = ItemHelper.asJsonItem(texture)
                    if (!Items.isNull(json)) {
                        type = TextureType.RAW
                        if (!dynamic) static = json!!
                    }
                } else if (!dynamic) {
                    static = parseMaterial(texture)
                }
            }
            return Texture(raw, texture, dynamic, static, type, meta)
        }

        private fun parseMaterial(material: String): ItemStack {
            val split = material.split(":", limit = 2)
            val data = split.getOrNull(1)?.toIntOrNull() ?: 0
            val id = split[0].toIntOrNull() ?: split[0].toUpperCase().replace("[ _]".toRegex(), "_")
            val builder = ItemBuilder(FALL_BACK)

            if (id is Int) {
                builder.material(id)
                builder.damage(data)
            } else {
                val name = id.toString()
                val xMaterial =
                    XMaterial.values().find { it.name.equals(name, true) }
                        ?: XMaterial.values()
                            .find { it -> it.legacy.any { it == name } }
                        ?: XMaterial.values()
                            .maxByOrNull { Strings.similarDegree(name, it.name) }

                if (xMaterial != null) {
                    return xMaterial.parseItem() ?: FALL_BACK
                } else {
                    builder.material(Material.valueOf(name))
                }
            }

            return builder.build()
        }

    }

}