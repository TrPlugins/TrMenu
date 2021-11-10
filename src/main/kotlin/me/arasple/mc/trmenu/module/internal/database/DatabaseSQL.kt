package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu
import taboolib.library.configuration.FileConfiguration
import taboolib.module.configuration.SecuredFile
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.util.concurrent.ConcurrentHashMap

// Almost a total copy from DatabaseSQLite
class DatabaseSQL : IndexedDatabase() {

    private val host = TrMenu.SETTINGS.getHost("Database.Type.SQL")

    val table = Table(TrMenu.SETTINGS.getString("Database.Type.SQL.table", "trmenu_user_data"), host) {
        add {
            name("user")
            type(ColumnTypeSQL.VARCHAR, 36) {
                options(ColumnOptionSQL.PRIMARY_KEY)
            }
        }
        add {
            name("data")
            type(ColumnTypeSQL.MEDIUMTEXT)
        }
    }

    val dataSource = host.createDataSource()
    val cache = ConcurrentHashMap<String, FileConfiguration>()

    init {
        table.workspace(dataSource) { createTable() }.run()
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