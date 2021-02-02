package me.arasple.mc.trmenu.module.internal.hook

import me.arasple.mc.trmenu.module.internal.hook.impl.HookHeadDatabase
import me.arasple.mc.trmenu.module.internal.hook.impl.HookOraxen
import me.arasple.mc.trmenu.module.internal.hook.impl.HookPlayerPoints
import me.arasple.mc.trmenu.module.internal.hook.impl.HookSkinsRestorer

/**
 * @author Arasple
 * @date 2021/1/26 22:04
 */
object HookPlugin {

    private val registry: List<HookAbstract> = listOf(
        HookHeadDatabase(),
        HookOraxen(),
        HookPlayerPoints(),
        HookSkinsRestorer(),
    )

    fun getHeadDatabase(): HookHeadDatabase {
        return registry[0] as HookHeadDatabase
    }

    fun getHookOraxen(): HookOraxen {
        return registry[1] as HookOraxen
    }

    fun getPlayerPoints(): HookPlayerPoints {
        return registry[2] as HookPlayerPoints
    }

    fun getSkinsRestorer(): HookSkinsRestorer {
        return registry[3] as HookSkinsRestorer
    }

}