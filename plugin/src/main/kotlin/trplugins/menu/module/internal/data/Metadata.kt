package trplugins.menu.module.internal.data

import trplugins.menu.TrMenu
import trplugins.menu.TrMenu.SETTINGS
import trplugins.menu.api.event.CustomDatabaseEvent
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.database.DatabaseSQLite
import trplugins.menu.module.internal.database.DatabaseMongodb
import trplugins.menu.module.internal.database.DatabaseSQL
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.Schedule
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * @author Arasple
 * @date 2021/1/27 11:46
 *
 * differences:
 *
 * <arguments> -> for each trplugins.menu session
 * <meta> -> only lost when the server is shut down
 * <data> -> storable, (support MongoDB)
 */
object Metadata {

    internal val meta = mutableMapOf<String, DataMap>()
    internal val data = mutableMapOf<String, DataMap>()

    @Config("data/globalData.yml")
    lateinit var globalData: Configuration

    // Copy in the Adyeshach
    val database by lazy {
        when (val db = SETTINGS.getString("Database.Method")?.uppercase()) {
            "LOCAL", "SQLITE", null -> DatabaseSQLite()
            "SQL" -> DatabaseSQL()
            "MONGODB" -> DatabaseMongodb()
            else -> {
                val event = CustomDatabaseEvent(db)
                event.call()
                event.database ?: error("\"${SETTINGS.getString("Database.Method")}\" not supported.")
            }
        }
    }

//    @TFunction.Cancel 暂不处理
    @Awake(LifeCycle.DISABLE)
    @Schedule(delay = 100, period = 20 * 30, async = true)
    fun save() {
        data.forEach { (playerName, dataMap) ->
            val player = Bukkit.getPlayerExact(playerName) ?: return@forEach
            pushData(player, dataMap)
        }
        globalData.saveToFile()
    }

    fun pushData(player: Player, dataMap: DataMap = getData(player)) {
        getLocalePlayer(player).let {
            if (it != null)
                dataMap.data.forEach { (key, value) -> it["TrMenu.Data.$key"] = value }
            else println("NullData: ${player.name}")
        }
        database.push(player)
    }

    @Suppress("DEPRECATION")
    fun getLocalePlayer(player: Player): Configuration? {
        return database.pull(player)
    }

    fun loadData(player: Player) {
        data[player.name] = DataMap(
            getLocalePlayer(player)!!.getConfigurationSection("TrMenu.Data")?.toMap()?.toMutableMap() ?: mutableMapOf()
        )
    }

    fun <T> getData(target: T): DataMap {
        return data.computeIfAbsent(getPlayerName(target)) { DataMap() }
    }

    fun <T> getMeta(target: T): DataMap {
        return meta.computeIfAbsent(getPlayerName(target)) { DataMap() }
    }

    fun setBukkitMeta(player: Player, key: String, value: String = "") {
        player.setMetadata(key, FixedMetadataValue(TrMenu.plugin, value))
    }

    fun byBukkit(player: Player, key: String): Boolean {
        return player.hasMetadata(key).also {
            if (it) player.removeMetadata(key, TrMenu.plugin)
        }
    }

    private fun <T> getPlayerName(target: T): String {
        return when (target) {
            is Player -> target.name
            is ProxyPlayer -> target.name
            is MenuSession -> target.placeholderPlayer.name
            else -> throw Exception("?????")
        }
    }

}