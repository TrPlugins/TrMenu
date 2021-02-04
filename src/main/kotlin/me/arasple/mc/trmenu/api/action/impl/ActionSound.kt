package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 11:12
 */
class ActionSound(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer, ";").forEach {
            val split = it.split("-")
            if (split.isNotEmpty()) {
                val sound = Sounds.matchSounds(split[0]).orElse(null)
                if (sound == null) {
                    TLocale.sendTo(player, "Menu.Action.Sound", it)
                    return
                }
                val volume: Float = split.getOrNull(1)?.toFloatOrNull() ?: 1f
                val pitch: Float = split.getOrNull(2)?.toFloatOrNull() ?: 1f
                sound.play(player, volume, pitch)
            }
        }
    }

    companion object {

        private val name = "(play)?-?sounds?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSound(value.toString(), option)
        }

        val registery = name to parser

    }

}