package trplugins.menu.api.receptacle.hook

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi

/**
 * TrMenu
 * trplugins.menu.api.receptacle.hook.HookFloodgate
 *
 * @author Score2
 * @since 2022/02/19 22:58
 */
class HookFloodgate private constructor() {

    fun isBedrockPlayer(player: Player): Boolean {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    }

    companion object {

        private var hook: HookFloodgate? = null

        fun isBedrockPlayer(player: Player): Boolean {
            if (hook == null) {
                Bukkit.getPluginManager().getPlugin("floodgate-bukkit") ?: return false
                hook = HookFloodgate()
            }
            return hook?.isBedrockPlayer(player) ?: false
        }
    }

}