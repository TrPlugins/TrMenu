package me.arasple.mc.trmenu.api.action.impl.hook.cronus

import ink.ptms.cronus.internal.program.effect.EffectParser
import ink.ptms.cronus.uranus.program.effect.Effect
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/20 7:07
 */
class ActionCronusEffect : Action("(cq|cronus)(-)?effect(s)?") {

    val effect: Effect
        get() {
            return EffectParser.parse(content)
        }

    override fun onExecute(player: Player) {
        if (HookInstance.getCronus().isHooked()) {
            effect.eval(
                    HookInstance.getCronus().nonProgram(player)
            )
        }
    }

}