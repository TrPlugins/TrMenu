package me.arasple.mc.trmenu.module.conf

import taboolib.library.configuration.YamlConfiguration
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import org.bukkit.ChatColor
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.setProperty
import java.io.File

/**
 * @project TrMenu
 *
 * @author Score2
 * @since 2021/10/22 11:27
 */
enum class MenuType(
    val suffixes: Array<String>,
    val color: ChatColor,
    private val serialization: (String) -> YamlConfiguration,
    private val deserialization: (YamlConfiguration) -> String
) {
    YAML(
        arrayOf("yaml", "yml"),
        ChatColor.AQUA,
        {
            YamlConfiguration().also { yaml -> yaml.loadFromString(it) }
        },
        {
            it.saveToString()
        }
    ),
    JSON(
        arrayOf("json"),
        ChatColor.RED,
        {
            YamlConfiguration().also { yaml -> yaml.setProperty("map", JsonParser().parse(it).asJsonObject.getProperty<LinkedTreeMap<String, JsonElement>>("members")!!.toMap()) }
        },
        {
            Gson().toJson(it.getProperty("map"))
        }
    ),
    ;

    fun serialize(file: File): YamlConfiguration {
        return serialize(file.readText())
    }

    fun serialize(text: String): YamlConfiguration {
        return serialization(text)
    }

    fun deserialize(yaml: YamlConfiguration): String {
        return deserialization(yaml)
    }
}