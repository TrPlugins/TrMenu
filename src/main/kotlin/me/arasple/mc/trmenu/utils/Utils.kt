package me.arasple.mc.trmenu.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.modules.configuration.property.Property
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Cancellable


/**
 * @author Arasple
 * @date 2020/5/30 12:26
 */
object Utils {

    fun asList(any: Any?): List<String> {
        if (any == null) return mutableListOf()
        val result = mutableListOf<String>()
        when (any) {
            is List<*> -> any.forEach { result.add(it.toString()) }
            is MemorySection -> any.getValues(false).forEach { result.add("${it.key}:${it.value}") }
            else -> result.add(any.toString())
        }
        return result
    }

    fun asAnyList(any: Any?): List<Any> {
        if (any == null) return mutableListOf()
        val result = mutableListOf<Any>()
        when (any) {
            is List<*> -> any.forEach { it?.let { any -> result.add(any) } }
            else -> result.add(any.toString())
        }
        return result
    }

    fun asIntList(any: Any?): List<Int> {
        if (any == null) return mutableListOf()
        val result = mutableListOf<Int>()
        when (any) {
            is List<*> -> any.forEach { result.add(it.toString().toInt()) }
            else -> result.add(any.toString().toInt())
        }
        return result
    }

    fun asIntRange(params: String): IntRange {
        try {
            params.split("-").let {
                val start = it[0].toInt()
                val end = if (it.size > 1) it[1].toInt() else start
                return IntRange(start, end)
            }
        } catch (e: NumberFormatException) {
            Msger.printErrors("INT-RANGE", e, params)
            return IntRange(0, 0)
        }
    }

    fun asArray(any: Any?): Array<String> = asList(any).toTypedArray()

    fun asIntArray(any: Any?): Array<Int> = asIntList(any).toTypedArray()

    fun asBoolean(any: Any?): Boolean = any.toString().toBoolean()

    fun asInt(any: Any?, def: Int): Int = NumberUtils.toInt(any.toString(), def)

    fun asLong(any: Any?, def: Long): Long = NumberUtils.toLong(any.toString(), def)

    @Suppress("UNCHECKED_CAST")
    fun <T> asLists(any: Any): List<List<T>> {
        val results = mutableListOf<List<T>>()
        when (any) {
            is List<*> -> {
                if (any.isNotEmpty()) {
                    if (any[0] is List<*>) any.forEach { results.add(it as List<T>) }
                    else results.add(any as List<T>)
                }
            }
            else -> results.add(listOf(any as T))
        }
        return results
    }

    fun isJson(string: String) = try {
        JsonParser().parse(string) is JsonObject
    } catch (e: Throwable) {
        false
    }

    fun isEventIgnoreCancelled(event: Cancellable): Boolean = TrMenu.SETTINGS.getBoolean("Events-Ignore-Cancelled.${event.javaClass.simpleName}", true) && event.isCancelled

    fun asSection(any: Any?): MemorySection? = YamlConfiguration().let {
        when (any) {
            is MemorySection -> return any
            is Map<*, *> -> {
                any.entries.forEach { entry -> it.set(entry.key.toString(), entry.value) }
                return@let it
            }
            is List<*> -> any.forEach { any ->
                val args = any.toString().split(Regex(":"), 2)
                if (args.size == 2) it.set(args[0], args[1])
                return@let it
            }
        }
        return@let null
    }

    fun getSectionKey(section: ConfigurationSection?, property: Property) = getSectionKey(section, property.regex, property.default, false)

    fun getSectionKey(section: ConfigurationSection?, regex: Regex, default: String, deep: Boolean) = section?.getKeys(deep)?.firstOrNull { it.matches(regex) } ?: default


}