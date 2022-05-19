package trplugins.menu.api.action.impl.send

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.library.xseries.XSound
import taboolib.module.lang.sendLang
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Sound
 *
 * @author Score2
 * @since 2022/02/14 12:30
 */
class Sound(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(play)?-?sounds?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        contents.stringContent().parseContentSplited(placeholderPlayer, ";").forEach {
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
                sound.play(player.cast<Player>(), volume, pitch)
            }
        }
    }
}