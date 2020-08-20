package me.arasple.mc.trmenu.modules.display.icon

import me.arasple.mc.trmenu.modules.function.script.Scripts
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/31 13:40
 */
data class IconProperty(var priority: Int, var condition: String, var inherit: Boolean, val display: IconDisplay, val clickHandler: IconClickHandler) {

    fun evalCondition(player: Player) = if (condition.isBlank()) true else Scripts.expression(player, condition).asBoolean()

}