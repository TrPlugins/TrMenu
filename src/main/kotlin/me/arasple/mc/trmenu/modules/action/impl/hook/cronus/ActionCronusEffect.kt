package me.arasple.mc.trmenu.modules.action.impl.hook.cronus

import ink.ptms.cronus.internal.program.effect.EffectParser
import me.arasple.mc.trmenu.modules.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/20 7:07
 */
class ActionCronusEffect : Action("") {

    override fun onExecute(player: Player) {
        EffectParser.parse(getContent(player))
    }

}