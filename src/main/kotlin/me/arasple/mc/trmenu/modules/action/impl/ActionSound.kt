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
		val split = getContent(player).split("-")
		if (split.isNotEmpty()) {
			val sound: Sounds
			try {
				sound = Sounds.valueOf(split[0])
			} catch (e: Throwable) {
				TLocale.sendToConsole("ERRORS.SOUND", getContent(player), player.name)
				return
			}

			var volume = 1.0f
			var pitch = 1.0f
			if (split.size >= 2) volume = NumberUtils.toFloat(split[1], 1f)
			if (split.size >= 3) pitch = NumberUtils.toFloat(split[2], 1f)
			sound.playSound(player, volume, pitch)
		}
	}

}