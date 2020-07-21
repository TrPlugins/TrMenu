package me.arasple.mc.trmenu.modules.function.catcher

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trmenu.data.MetaPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * @author Arasple
 * @date 2020/7/21 10:12
 */
@TListener
class ListenerCatcher : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        val catcher = InputCatchers.getCatcher(player) ?: return
        checkFinish(player, catcher)

        catcher.currentStage(player)?.let {
            if (it.type == InputCatcher.Type.CHAT) {
                val result = it.reaction(player, MetaPlayer.filterInput(e.message))
                if (result == InputCatcher.Stage.Result.PROCCED) {
                    catcher.nextStage(player)
                } else if (result == InputCatcher.Stage.Result.RETRY) {
                    catcher.currentStage(player)?.before(player)
                }
                e.isCancelled = true
            }
        }
    }

    private fun checkFinish(player: Player, catcher: InputCatcher) {
        if (catcher.isFinal(player)) {
            InputCatchers.finish(player)
            return
        }
    }

}