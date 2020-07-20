package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.locale.TLocale
import org.black_ixx.playerpoints.PlayerPoints
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin

/**
 * @author Arasple
 * @date 2020/3/2 10:54
 */
object HookPlayerPoints {

    private const val PLUGIN_NAME = "PlayerPoints"
    private var IS_HOOKED = false
    private var PLAYER_POINTS: Plugin? = null

    fun isHooked() = IS_HOOKED

    fun init() {
        PLAYER_POINTS = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)
        IS_HOOKED = PLAYER_POINTS?.isEnabled ?: false
        if (IS_HOOKED) {
            TLocale.sendToConsole("HOOKED", PLUGIN_NAME)
        }
    }

    fun addPoints(player: OfflinePlayer, amount: Int) {
        if (isHooked()) {
            (PLAYER_POINTS as PlayerPoints).api.give(player.uniqueId, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun takePoints(player: OfflinePlayer, amount: Int) {
        if (isHooked()) {
            (PLAYER_POINTS as PlayerPoints).api.take(player.uniqueId, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun setPoints(player: OfflinePlayer, amount: Int) {
        if (isHooked()) {
            (PLAYER_POINTS as PlayerPoints).api.set(player.uniqueId, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun getPoints(player: OfflinePlayer): Int = if (isHooked()) {
        (PLAYER_POINTS as PlayerPoints).api.look(player.uniqueId)
    } else {
        TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        -1
    }

    fun hasPoints(player: OfflinePlayer, amount: Int): Boolean = getPoints(player) >= amount

}