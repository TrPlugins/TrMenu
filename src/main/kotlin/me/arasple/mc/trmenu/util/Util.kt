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
