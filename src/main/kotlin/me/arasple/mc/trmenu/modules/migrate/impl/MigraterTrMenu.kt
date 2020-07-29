package me.arasple.mc.trmenu.modules.migrate.impl

import me.arasple.mc.trmenu.configuration.property.Property
import me.arasple.mc.trmenu.modules.migrate.Migrater
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * @author Arasple
 * @date 2020/7/21 8:18
 */
class MigraterTrMenu(val file: File) : Migrater {

    override fun run(): YamlConfiguration {
        val legacy = loadConfiguration(file)

        Migrater.transferValue(
            legacy,
            // MenuEvents.Open
            Migrater.TransferValueSet(
                "open-requirement(s)?",
                "Open-Requirement",
                "${Property.EVENTS}.${Property.EVENT_OPEN}.requirement"
            ),
            Migrater.TransferValueSet(
                "open-action(s)?",
                "Open-Actions",
                "${Property.EVENTS}.${Property.EVENT_OPEN}.actions"
            ),
            Migrater.TransferValueSet(
                "open-deny-action(s)?",
                "Open-Deny-Actions",
                "${Property.EVENTS}.${Property.EVENT_OPEN}.deny-actions"
            ),
            // MenuEvents.Close
            Migrater.TransferValueSet(
                "close-requirement(s)?",
                "Close-Requirement",
                "${Property.EVENTS}.${Property.EVENT_CLOSE}.requirement"
            ),
            Migrater.TransferValueSet(
                "close-action(s)?",
                "Close-Actions",
                "${Property.EVENTS}.${Property.EVENT_CLOSE}.actions"
            ),
            Migrater.TransferValueSet(
                "close-deny-action(s)?",
                "Close-Deny-Actions",
                "${Property.EVENTS}.${Property.EVENT_CLOSE}.deny-actions"
            ),
            // Menu Bound
            Migrater.TransferValueSet(
                "open-command(s)?",
                "Open-Commands",
                "${Property.BINDINGS}.${Property.BINDING_COMMANDS}"
            ),
            Migrater.TransferValueSet(
                "option(s)?.bind-item-lore",
                "Options.Bind-Item-Lore",
                "${Property.BINDINGS}.${Property.BINDING_ITEMS}"
            )
        )

        Migrater.removeValue(
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
                append("Date: ${Migrater.getExactDate()}\n")
                appendLine()
            }
        )

        return legacy
    }

}