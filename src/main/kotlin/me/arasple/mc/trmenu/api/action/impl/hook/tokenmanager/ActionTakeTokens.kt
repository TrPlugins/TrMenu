package me.arasple.mc.trmenu.api.action.impl.hook.tokenmanager

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.modules.function.hook.HookTokenManager
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/26 16:34
 */
class ActionTakeTokens : Action("(take|remove|withdraw)(-)?token(s)?") {

    override fun onExecute(player: Player) = NumberUtils.toLong(getContent(player), -1).let {
        if (it > 0) HookTokenManager.takeTokens(player, it)
    }

}