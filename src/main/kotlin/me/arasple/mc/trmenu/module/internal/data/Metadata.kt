package me.arasple.mc.trmenu.module.internal.data

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.TrMenu.SETTINGS
import me.arasple.mc.trmenu.api.event.CustomDatabaseEvent
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.database.DatabaseLocal
import me.arasple.mc.trmenu.module.internal.database.DatabaseMongodb
import me.arasple.mc.trmenu.util.reloadable
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.library.configuration.FileConfiguration
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import java.util.*

/**
 * @author Arasple
 * @date 2021/1/27 11:46
 *
 * differences:
 *
 * <arguments> -> for each menu session
 * <meta> -> only lost when the server is shut down
 * <data> -> storable, (support MongoDB)
 */
object Metadata {

    internal val meta = mutableMapOf<String, DataMap>()
    internal val data = mutableMapOf<String, DataMap>()

    @Config("data/globalData.yml")
    lateinit var globalData: SecuredFile

    // Copy in the Adyeshach
    val database by lazy {
        when (val db = SETTINGS.getString("Database.Method")!!.uppercase(Locale.getDefault())) {
            "LOCAL" -> DatabaseLocal()
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
            getLocalePlayer(player).let {
                if (it != null)
                    dataMap.data.forEach { (key, value) -> it.set("TrMenu.Data.$key", value) }
                else println("NullData: $playerName")
            }
            database.push(player)
        }
        globalData.saveToFile()
    }

    @Suppress("DEPRECATION")
    fun getLocalePlayer(player: Player): FileConfiguration? {
        return database.pull(player)
    }

    fun loadData(player: Player) {
        data[player.name] = DataMap(
            getLocalePlayer(player)!!.getConfigurationSection("TrMenu.Data")?.getValues(true) ?: mutableMapOf()
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
            is MenuSession -> target.placeholderPlayer.name
            else -> throw Exception("?????")
        }
    }

}