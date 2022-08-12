package trplugins.menu.module.internal.hook.impl

import trplugins.menu.module.internal.hook.HookAbstract
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi

/**
 * @author Arasple
 * @date 2021/2/4 20:12
 */
class HookFloodgate : HookAbstract() {

    fun isBedrockPlayer(player: Player): Boolean {
        return if (checkHooked()) FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId) else false
    }

    override fun getPluginName(): String {
        return "floodgate-bukkit"
    }

}