package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/1 19:51
 */
class ActionSound : Action("(play)?(-)?sound") {

    override fun onExecute(player: Player) {
        getSplitedBySemicolon(player).forEach {
            val split = it.split("-")
            if (split.isNotEmpty()) {
                val sound = Sounds.matchSounds(split[0]).orElse(null)
                if (sound == null) {
                    TLocale.sendToConsole("ERRORS.SOUND", it, player.name)
                    return
                }

                var volume = 1.0f
                var pitch = 1.0f
                if (split.size >= 2) volume = NumberUtils.toFloat(split[1], 1f)
                if (split.size >= 3) pitch = NumberUtils.toFloat(split[2], 1f)
                sound.play(player, volume, pitch)
            }
        }


    }

}