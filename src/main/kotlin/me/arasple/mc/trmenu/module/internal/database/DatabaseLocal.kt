package me.arasple.mc.trmenu.module.internal.database

import org.bukkit.entity.Player
import taboolib.common.platform.getDataFolder
import taboolib.library.configuration.FileConfiguration
import taboolib.module.configuration.SecuredFile
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author sky
 * @Since 2020-08-14 14:46
 */
class DatabaseLocal : Database() {

    val host = File(getDataFolder(), "data.db").getHost()

    val table = Table("npc", host) {
        add { id() }
        add {
            name("user")
            type(ColumnTypeSQLite.TEXT)
        }
        add {
            name("data")
            type(ColumnTypeSQLite.TEXT)
        }
    }

    val dataSource = host.createDataSource()
    val cache = ConcurrentHashMap<String, FileConfiguration>()

    override fun pull(player: String): FileConfiguration {
        return cache.computeIfAbsent(player) {
            table.workspace(dataSource) {
                select { "user" eq player }
            }.firstOrNull {
                SecuredFile.loadConfiguration(getString("data"))
            } ?: SecuredFile()
        }
    }

    override fun push(player: String) {
        val file = cache[player] ?: return
        if (table.workspace(dataSource) { select { "user" eq player } }.find()) {
            table.workspace(dataSource) {
                update {
                    set("data", file.saveToString())
                    where {
                        "user" eq player
                    }
                }
            }.run()
        } else {
            table.workspace(dataSource) {
                insert("user", "data") {
                    value(player, file.saveToString())
                }
            }.run()
        }
    }

    override fun release(player: String) {
        cache.remove(player)
    }
}