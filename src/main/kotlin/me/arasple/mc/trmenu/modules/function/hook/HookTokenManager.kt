package me.arasple.mc.trmenu.modules.function.hook

import io.izzel.taboolib.module.locale.TLocale
import me.realized.tokenmanager.api.TokenManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * @author Arasple
 * @date 2020/8/26 16:20
 */
object HookTokenManager {

    private const val PLUGIN_NAME = "TokenManager"
    private var IS_HOOKED = false
    private var TOKEN_MANAGER: Plugin? = null

    fun isHooked() = IS_HOOKED

    fun init() {
        TOKEN_MANAGER = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)
        IS_HOOKED = TOKEN_MANAGER?.isEnabled ?: false
        if (IS_HOOKED) {
            TLocale.sendToConsole("PLUGIN.HOOKED", PLUGIN_NAME)
        }
    }

    fun addTokens(player: Player, amount: Long) {
        if (isHooked()) {
            (TOKEN_MANAGER as TokenManager).addTokens(player, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun takeTokens(player: Player, amount: Long) {
        if (isHooked()) {
            (TOKEN_MANAGER as TokenManager).removeTokens(player, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun setTokens(player: Player, amount: Long) {
        if (isHooked()) {
            (TOKEN_MANAGER as TokenManager).setTokens(player, amount)
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
    }

    fun getTokens(player: Player): Long = if (isHooked()) {
        (TOKEN_MANAGER as TokenManager).getTokens(player).asLong
    } else {
        TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        -1L
    }

    fun hasTokens(player: Player, amount: Long): Boolean = getTokens(player) >= amount

}