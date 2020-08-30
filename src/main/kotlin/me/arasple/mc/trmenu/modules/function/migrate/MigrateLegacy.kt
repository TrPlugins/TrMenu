package me.arasple.mc.trmenu.modules.function.migrate

import me.arasple.mc.trmenu.modules.conf.property.Property
import me.arasple.mc.trmenu.util.Utils
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * @author Arasple
 * @date 2020/8/16 21:04
 */
object MigrateLegacy {

    fun run(file: File): YamlConfiguration {
        val legacy = YamlConfiguration.loadConfiguration(file)

        transferValue(
                legacy,
                // MenuEvents.Open
                TransferValueSet(
                        "open-requirement(s)?",
                        "Open-Requirement",
                        "${Property.EVENTS}.${Property.EVENT_OPEN}.requirement"
                ),
                TransferValueSet(
                        "open-action(s)?",
                        "Open-Actions",
                        "${Property.EVENTS}.${Property.EVENT_OPEN}.actions"
                ),
                TransferValueSet(
                        "open-deny-action(s)?",
                        "Open-Deny-Actions",
                        "${Property.EVENTS}.${Property.EVENT_OPEN}.deny-actions"
                ),
                // MenuEvents.Close
                TransferValueSet(
                        "close-requirement(s)?",
                        "Close-Requirement",
                        "${Property.EVENTS}.${Property.EVENT_CLOSE}.requirement"
                ),
                TransferValueSet(
                        "close-action(s)?",
                        "Close-Actions",
                        "${Property.EVENTS}.${Property.EVENT_CLOSE}.actions"
                ),
                TransferValueSet(
                        "close-deny-action(s)?",
                        "Close-Deny-Actions",
                        "${Property.EVENTS}.${Property.EVENT_CLOSE}.deny-actions"
                ),
                // Menu Bound
                TransferValueSet(
                        "open-command(s)?",
                        "Open-Commands",
                        "${Property.BINDINGS}.${Property.BINDING_COMMANDS}"
                ),
                TransferValueSet(
                        "option(s)?.bind-item-lore",
                        "Options.Bind-Item-Lore",
                        "${Property.BINDINGS}.${Property.BINDING_ITEMS}"
                )
        )

        removeValue(
                legacy,
                true,
                "option(s)?.lock-player-inv",
                "option(s)?.update-inventory",
                "option(s)?.force-transfer-arg(s)?",
        )

        legacy.options().header(
                buildString {
                    appendLine()
                    append("Migrated from TrMenu v1.x, by TrMenu v2\n")
                    append("Date: ${Migrate.getExactDate()}\n")
                    appendLine()
                }
        )
        return legacy
    }

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

    class TransferValueSet(val regex: String, val default: String, val newKey: String, val deep: Boolean) {

        constructor(regex: String, default: String, newKey: String) : this("(?i)$regex", default, newKey, false)

    }


}