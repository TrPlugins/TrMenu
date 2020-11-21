package me.arasple.mc.trmenu.modules.function.hook

import io.izzel.taboolib.module.compat.EconomyHook
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.function.hook.impl.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/27 19:16
 */
abstract class HookInstance {

    fun isHooked(): Boolean {
        val plugin = Bukkit.getPluginManager().getPlugin(getDepend())
        return plugin != null && plugin.isEnabled
    }

    abstract fun getDepend(): String

    abstract fun initialization()

    companion object {

        private val hooks = mapOf(
                "LuckPerms" to HookLuckPerms(),
                "CMI" to HookCMI(),
                "Cronus" to HookCronus(),
                "HeadDatabase" to HookHeadDatabase(),
                "LuckPerms" to HookLuckPerms(),
                "PlayerPoints" to HookPlayerPoints(),
                "SkinsRestorer" to HookSkinsRestorer(),
                "TokenManager" to HookTokenManager()
        )

        fun init() {
            hooks.values.forEach {
                if (it.isHooked()) {
                    TLocale.sendToConsole("PLUGIN.HOOKED", it.getDepend())
                    it.initialization()
                }
            }
        }

        fun get(pluginName: String): HookInstance? {
            return hooks[pluginName]
        }

        fun getCMI(): HookCMI {
            return get("CMI") as HookCMI
        }

        fun getCronus(): HookCronus {
            return get("Cronus") as HookCronus
        }

        fun getPlayerPoints(): HookPlayerPoints {
            return get("PlayerPoints") as HookPlayerPoints
        }

        fun getTokenManager(): HookTokenManager {
            return get("TokenManager") as HookTokenManager
        }

        fun getHeadDatabase(): HookHeadDatabase {
            return get("HeadDatabase") as HookHeadDatabase
        }

        fun getSkinsRestorer(): HookSkinsRestorer {
            return get("SkinsRestorer") as HookSkinsRestorer
        }

        /*
        ECONOMY
         */
        fun get(player: Player): Double {
            return if (getCMI().isHooked()) getCMI().getUser(player)?.economyAccount?.balance
                    ?: -1.0 else EconomyHook.get(
                    player
            )
        }

        fun add(player: Player, value: Double) {
            if (getCMI().isHooked()) {
                getCMI().getUser(player)?.economyAccount?.deposit(value)
            } else {
                EconomyHook.add(player, value)
            }
        }

        fun remove(player: Player, value: Double) {
            if (getCMI().isHooked()) {
                getCMI().getUser(player)?.economyAccount?.withdraw(value)
            } else {
                EconomyHook.remove(player, value)
            }
        }

        fun set(player: Player, value: Double) {
            if (getCMI().isHooked()) {
                getCMI().getUser(player)?.economyAccount?.setBalance(value)
            } else {
                EconomyHook.set(player, value)
            }
        }

    }

}