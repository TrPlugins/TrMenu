package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.util.lite.Effects
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/28 16:59
 */
class ActionParticle : Action("(effect|particle)(s)?") {

    val effects: Effects
        get() {
            return Effects.parse(content)
        }

    override fun onExecute(player: Player) = effects.center(player.location).playAsync()

}