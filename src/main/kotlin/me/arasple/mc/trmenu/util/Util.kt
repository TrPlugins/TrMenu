package me.arasple.mc.trmenu.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import taboolib.common.platform.warning
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @date 2021/2/19 22:40
 */
fun Throwable.print(title: String) {
    println("§c[TrMenu] §8$title")
    println("         §8${localizedMessage}")
    stackTrace.forEach {
        println("         §8$it")
    }
}

fun ItemStack.readBuilder() = ItemBuilder(XMaterial.matchXMaterial(this.type)).also { builder ->
    fun <T> applyToList(origin: MutableList<T>?, target: MutableList<T>?) {
        target?.clear()
        origin?.let { target?.addAll(it) }
    }
    fun <T> applyToList(origin: MutableSet<T>?, target: MutableList<T>?) {
        target?.clear()
        origin?.let { target?.addAll(it) }
    }
    fun <K, V> applyToMap(origin: MutableMap<K, V>?, target: MutableMap<K, V>?) {
        target?.clear()
        origin?.let { target?.putAll(it) }
    }
    val itemMeta = itemMeta
    builder.name = itemMeta?.displayName
    applyToList(itemMeta?.lore, builder.lore)
    applyToList(itemMeta?.itemFlags, builder.flags)
    if (itemMeta is EnchantmentStorageMeta) {
        applyToMap(itemMeta.storedEnchants, builder.enchants)
    } else {
        applyToMap(itemMeta?.enchants, builder.enchants)
    }

    when(itemMeta) {
        is LeatherArmorMeta -> {
            builder.color = itemMeta.color
        }
        is PotionMeta -> {
            builder.color = itemMeta.color
            applyToList(builder.potions, itemMeta.customEffects)
            builder.potionData = itemMeta.basePotionData
        }
        is SkullMeta -> {
            if (itemMeta.owner != null) {
                builder.skullOwner = itemMeta.owner
            }
            itemMeta.getProperty<GameProfile>("profile").also {
                if (it != null) {
                    builder.skullTexture = it.properties.getProperty<Property>("textures")?.value?.let { it1 -> ItemBuilder.SkullTexture(it1, it.id) }
                }
            }
        }
    }
    try {
        builder.isUnbreakable = itemMeta?.isUnbreakable ?: false
    } catch (ex: NoSuchMethodError) {
        try {
            builder.isUnbreakable = itemMeta?.invokeMethod<Boolean>("spigot")!!.invokeMethod<Boolean>("isUnbreakable") ?: false
        } catch (ex: NoSuchMethodException) {
            warning("Unbreakable not supported yet.")
        }
    }
    try {
        if (itemMeta is SpawnEggMeta && itemMeta.spawnedType != null) {
            builder.spawnType = itemMeta.spawnedType
        }
    } catch (ex: NoClassDefFoundError) {
        warning("SpawnEggMeta not supported yet.")
    }
    try {
        if (itemMeta is BannerMeta && itemMeta.patterns.isNotEmpty()) {
            applyToList(itemMeta.patterns, builder.patterns)
        }
    } catch (ex: NoClassDefFoundError) {
        warning("BannerMeta not supported yet.")
    }
    try {
        itemMeta?.invokeMethod<Int>("getCustomModelData").let {
            if (it == -1) {
                builder.customModelData = it
            }
        }
    } catch (ex: NoSuchMethodException) {
        warning("CustomModelData not supported yet.")
    }
}