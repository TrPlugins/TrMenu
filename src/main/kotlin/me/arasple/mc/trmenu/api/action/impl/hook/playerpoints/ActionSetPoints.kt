package me.arasple.mc.trmenu.api.action.impl.hook.playerpoints

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.function.hook.HookPlayerPoints
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:24
 */
class ActionSetPoints : Action("set(-)?point(s)?") {

    override fun onExecute(player: Player) = NumberUtils.toInt(getContent(player), -1).let {
        if (it > 0) HookPlayerPoints.setPoints(player, it)
    }

}