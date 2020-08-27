package me.arasple.mc.trmenu.modules.function.hook.impl

import com.Zrips.CMI.CMI
import com.Zrips.CMI.Containers.CMIUser
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/27 19:47
 */
class HookCMI : HookInstance() {

    override fun getDepend(): String {
        return "CMI"
    }

    override fun initialization() {

    }

    fun getUser(player: Player): CMIUser? {
        return CMI.getInstance().playerManager.getUser(player)
    }

}