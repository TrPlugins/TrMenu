package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/27 20:26
 */
class ActionDelay : Action("delay|wait") {

    fun getDelay(player: Player) = NumberUtils.toLong(getContent(player), 0)

}