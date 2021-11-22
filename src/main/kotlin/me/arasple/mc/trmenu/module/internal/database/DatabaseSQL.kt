package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.entity.Player
import taboolib.library.configuration.FileConfiguration
import taboolib.module.configuration.SecuredFile
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.util.concurrent.ConcurrentHashMap

// Almost a total copy from DatabaseSQLite
class DatabaseSQL : Database() {

    private val host = TrMenu.SETTINGS.getHost("Database.Type.SQL")

    val table = Table(TrMenu.SETTINGS.getString("Database.Type.SQL.table", "trmenu_user_data")!!, host) {
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

    override fun pull(player: Player, indexPlayer: String): FileConfiguration {
        return cache.computeIfAbsent(indexPlayer) {
            table.workspace(dataSource) {
                select { where { "user" eq indexPlayer } }
            }.firstOrNull {
                SecuredFile.loadConfiguration(getString("data"))
            } ?: SecuredFile()
        }
    }

    override fun push(player: Player, indexPlayer: String) {
        val file = cache[indexPlayer] ?: return
        if (table.workspace(dataSource) { select { where { "user" eq indexPlayer } } }.find()) {
            table.workspace(dataSource) {
                update {
                    set("data", file.saveToString())
                    where {
                        "user" eq indexPlayer
                    }
                }
            }.run()
        } else {
            table.workspace(dataSource) {
                insert("user", "data") {
                    value(indexPlayer, file.saveToString())
                }
            }.run()
        }
    }

    override fun release(player: Player, indexPlayer: String) {
        cache.remove(indexPlayer)
    }
}