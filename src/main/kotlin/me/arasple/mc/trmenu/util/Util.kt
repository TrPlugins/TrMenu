package me.arasple.mc.trmenu.util

import taboolib.library.configuration.MemorySection

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

val Boolean.trueOrNull get() = if (this) true else null

// 未来需要改进该功能
fun String.parseSimplePlaceholder(map: Map<Regex, String>): String {
    var raw = this
    map.forEach { raw = raw.replace(it.key, it.value) }
    return raw
}

// 未来需要改进该功能
fun String.parseIconId(iconId: String) = parseSimplePlaceholder(mapOf("(?i)@iconId@".toRegex() to iconId))

fun MemorySection.ignoreCase(path: String) = getKeys(true).find { it.equals(path, ignoreCase = true) } ?: path