package me.arasple.mc.trmenu.module.conf

import taboolib.library.configuration.YamlConfiguration
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import org.bukkit.ChatColor
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.SecuredFile
import taboolib.module.configuration.toml.TomlFile
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
    private val serialization: (String) -> Configuration,
    private val deserialization: (Configuration) -> String
) {
    YAML(
        arrayOf("yaml", "yml"),
        ChatColor.AQUA,
        {
            SecuredFile().also { yaml -> yaml.loadFromString(it) }
        },
        {
            it.saveToString()
        }
    ),
    JSON(
        arrayOf("json"),
        ChatColor.GOLD,
        {
            SecuredFile().also { yaml -> yaml.setProperty("map", Gson().fromJson(JsonParser().parse(it).asJsonObject, Any::class.java)) }
        },
        {
            Gson().toJson(it.getProperty<Map<String, Any>>("map"))
        }
    ),
    TOML(
        arrayOf("toml"),
        ChatColor.GRAY,
        {
            TomlFile().also { toml -> toml.loadFromString(it) }
        },
        {
            it.saveToString()
        }
    )
    ;

    fun serialize(file: File): Configuration {
        return serialize(file.readText())
    }

    fun serialize(text: String): Configuration {
        return serialization(text)
    }

    fun deserialize(yaml: Configuration): String {
        return deserialization(yaml)
    }
}