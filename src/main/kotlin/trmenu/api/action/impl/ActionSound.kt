package trmenu.api.action.impl

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import org.bukkit.entity.Player
import taboolib.library.xseries.XSound
import taboolib.platform.util.sendLang
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/31 11:12
 */
class ActionSound(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        parseContentSplited(placeholderPlayer, ";").forEach {
            val split = it.split("-")
            if (split.isNotEmpty()) {
                val sound: XSound
                try {
                    sound = XSound.valueOf(split[0])
                } catch (t: Throwable) {
                    player.sendLang("Menu-Action-Sound", it)
                    return
                }
                val volume: Float = split.getOrNull(1)?.toFloatOrNull() ?: 1f
                val pitch: Float = split.getOrNull(2)?.toFloatOrNull() ?: 1f
                sound.play(player, volume, pitch)
            }
        }
    }

    companion object : ActionDesc {

        override val name = "(play)?-?sounds?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSound(value.toString(), option)
        }

    }

}