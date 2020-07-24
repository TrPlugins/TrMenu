package me.arasple.mc.trmenu.modules.migrate

import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2020/7/21 8:02
 */
interface Migrater {


    fun run(): YamlConfiguration

    fun loadConfiguration(file: File) = YamlConfiguration().let {
        it.load(file)
        return@let it
    }

    companion object {

        val exact = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        fun getExactDate(): String = exact.format(System.currentTimeMillis())

        fun transferValue(legacy: YamlConfiguration, vararg transferValueSet: TransferValueSet) {
            transferValueSet.forEach { set ->
                Utils.getSectionKey(legacy, set.regex.toRegex(), set.default, set.deep).let {
                    val value = legacy.get(it) ?: return@let
                    legacy.set(it, null)
                    legacy.set(set.newKey, value)
                }
            }
        }

        fun removeValue(legacy: YamlConfiguration, deep: Boolean, vararg keys: String) = keys.forEach {
            Utils.getSectionKey(legacy, "(?i)$it".toRegex(), "", deep).let { key ->
                if (key.isNotBlank())
                    legacy.set(key, null)
            }
        }

    }

    class TransferValueSet(val regex: String, val default: String, val newKey: String, val deep: Boolean) {

        constructor(regex: String, default: String, newKey: String) : this("(?i)$regex", default, newKey, false)

    }

}