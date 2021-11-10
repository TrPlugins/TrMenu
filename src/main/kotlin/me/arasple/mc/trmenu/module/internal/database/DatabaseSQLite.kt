package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu
import taboolib.common.platform.function.getDataFolder
import taboolib.library.configuration.FileConfiguration
import taboolib.module.configuration.SecuredFile
import taboolib.module.database.ColumnOptionSQLite
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author sky
 * @Since 2020-08-14 14:46
 */
class DatabaseSQLite : IndexedDatabase() {

    private val host by lazy {
        val target = File(getDataFolder(), "data/${TrMenu.SETTINGS.getString("Database.Type.SQLite.file-name", "data")}.db")
        val origin = File(getDataFolder(), "data.db")

        // 迁移到新目录
        if (origin.exists()) {
            origin.copyTo(target)
            origin.delete()
        }

        target.getHost()
    }

    val table = Table(TrMenu.SETTINGS.getString("Database.Type.SQLite.table", "npc"), host) {
        add {
            name("user")
            type(ColumnTypeSQLite.TEXT, 36) {
                options(ColumnOptionSQLite.PRIMARY_KEY)
            }
        }
        add {
            name("data")
            type(ColumnTypeSQLite.TEXT)
        }
    }

    val dataSource = host.createDataSource()
    val cache = ConcurrentHashMap<String, FileConfiguration>()

    init {
        table.workspace(dataSource) { createTable(true) }.run()
    }

    override fun pull(player: String): FileConfiguration {
        return cache.computeIfAbsent(player) {
            table.workspace(dataSource) {
                select { where { "user" eq player } }
            }.firstOrNull {
                SecuredFile.loadConfiguration(getString("data"))
            } ?: SecuredFile()
        }
    }

    override fun push(player: String) {
        val file = cache[player] ?: return
        if (table.workspace(dataSource) { select { where { "user" eq player } } }.find()) {
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