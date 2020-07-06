package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 20:39
 */
class ActionActionbar : Action("action(bar)?(s)?") {

	override fun onExecute(player: Player) = TLocale.Display.sendActionBar(player, getContent(player))

}