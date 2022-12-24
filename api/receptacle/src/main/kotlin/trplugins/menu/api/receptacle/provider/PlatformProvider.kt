package trplugins.menu.api.receptacle.provider

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi

/**
 * @author Rubenicos
 * @since 2022/05/30 20:36
 */
abstract class PlatformProvider {

    open fun isBedrockPlayer(player: Player): Boolean {
        return false
    }

    companion object {

        var provider: PlatformProvider? = null

        init {
            compute()
        }

        fun compute() {
            if (provider != null) {
                return
            }
            provider = if (Bukkit.getPluginManager().getPlugin("floodgate-bukkit") != null || Bukkit.getPluginManager().getPlugin("floodgate") != null) {
                // Use floodgate api by default
                object : PlatformProvider() {
                    override fun isBedrockPlayer(player: Player): Boolean {
                        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
                    }
                }
            } else {
                // Use uuid comparator instead
                object : PlatformProvider() {
                    override fun isBedrockPlayer(player: Player): Boolean {
                        return player.uniqueId.toString().startsWith("00000000-0000-0000")
                    }
                }
            }
        }

        fun isBedrockPlayer(player: Player): Boolean {
            return provider != null && provider?.isBedrockPlayer(player) ?: false
        }
    }
}