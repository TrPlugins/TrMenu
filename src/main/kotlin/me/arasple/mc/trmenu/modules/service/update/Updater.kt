package me.arasple.mc.trmenu.modules.service.update

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.IO
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.sendLocale
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * @author Arasple
 * @date 2020/7/28 18:30
 */
object Updater {

    val API_URL = "https://api.github.com/repos/Arasple/${TrMenu.plugin.name}/releases/latest"
    val DESCRIPTION = TrMenu.plugin.description
    val CURRENT_VERSION = NumberUtils.toDouble(DESCRIPTION.version.split("-")[0], -1.0)
    var LATEST_VERSION = -1.0
    var NOTIFY = false
    var NOTIFIES = mutableSetOf<UUID>()

    @TFunction.Init
    fun init() {
        if (CURRENT_VERSION < 0) Bukkit.shutdown()
    }

    @TSchedule(delay = 20, period = 10 * 60 * 20, async = true)
    private fun grabInfo() {
        if (LATEST_VERSION > 0) {
            return
        }
        val read: String
        try {
            URL(API_URL).openStream().use { inputStream ->
                BufferedInputStream(inputStream).use { bufferedInputStream ->
                    read = IO.readFully(bufferedInputStream, StandardCharsets.UTF_8)
                    val json = JsonParser().parse(read) as JsonObject
                    val latestVersion = json.get("tag_name").asDouble
                    if (latestVersion > CURRENT_VERSION) {
                        LATEST_VERSION = latestVersion
                        if (LATEST_VERSION < 0) Bukkit.shutdown()
                        if (!NOTIFY) {
                            NOTIFY = true
                            TLocale.sendToConsole("PLUGIN.UPDATE", LATEST_VERSION)
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }

    @TListener
    class Listen : Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onJoin(e: PlayerJoinEvent) {
            val player = e.player
            if (player.isOp && LATEST_VERSION > CURRENT_VERSION && !NOTIFIES.contains(player.uniqueId)) {
                player.sendLocale("PLUGIN.UPDATE", LATEST_VERSION)
                NOTIFIES.add(player.uniqueId)
            }
        }

    }

}