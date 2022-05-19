package trplugins.menu.module.internal.database

import trplugins.menu.TrMenu
import org.bukkit.entity.Player
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
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
class DatabaseSQLite : Database() {

    private val host by lazy {
        val target = File(getDataFolder(), "data/${TrMenu.SETTINGS.getString("Database.Type.SQLite.file-name", "data")}.db")
        val origin = File(getDataFolder(), "data.db")

        // 迁移到新目录
        if (origin.exists() && !target.exists()) {
            origin.copyTo(target)
            origin.delete()
        }

        target.getHost()
    }

    val table = Table(TrMenu.SETTINGS.getString("Database.Type.SQLite.table", "data")!!, host) {
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
    val cache = ConcurrentHashMap<String, Configuration>()

    init {
        table.workspace(dataSource) { createTable(true) }.run()
    }

    override fun pull(player: Player, indexPlayer: String): Configuration {
        return cache.computeIfAbsent(indexPlayer) {
            table.workspace(dataSource) {
                select { where { "user" eq indexPlayer } }
            }.firstOrNull {
                Configuration.loadFromString(getString("data"))
            } ?: Configuration.empty()
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