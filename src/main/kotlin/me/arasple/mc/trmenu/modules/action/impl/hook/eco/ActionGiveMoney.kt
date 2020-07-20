package me.arasple.mc.trmenu.modules.action.impl.hook.eco

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.compat.EconomyHook
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:24
 */
class ActionGiveMoney : Action("(give|add|deposit)(-)?(money|eco|coin)(s)?") {

	override fun onExecute(player: Player) = NumberUtils.toDouble(getContent(player), -1.0).let {
		if (it > 0) EconomyHook.add(player, it)
	}

}