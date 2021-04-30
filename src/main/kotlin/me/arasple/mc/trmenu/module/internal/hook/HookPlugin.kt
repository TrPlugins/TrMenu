package me.arasple.mc.trmenu.module.internal.hook

import me.arasple.mc.trmenu.module.internal.hook.impl.*

/**
 * @author Arasple
 * @date 2021/1/26 22:04
 */
object HookPlugin {

    private val registry: Array<HookAbstract> = arrayOf(
        HookHeadDatabase(),
        HookOraxen(),
        HookPlayerPoints(),
        HookSkinsRestorer(),
        HookItemsAdder(),
        HookFloodgate(),
        HookVault(),
        HookFastScript(),
        HookOptEco
    )

    fun getHeadDatabase(): HookHeadDatabase {
        return registry[0] as HookHeadDatabase
    }

    fun getOraxen(): HookOraxen {
        return registry[1] as HookOraxen
    }

    fun getPlayerPoints(): HookPlayerPoints {
        return registry[2] as HookPlayerPoints
    }

    fun getSkinsRestorer(): HookSkinsRestorer {
        return registry[3] as HookSkinsRestorer
    }

    fun getItemsAdder(): HookItemsAdder {
        return registry[4] as HookItemsAdder
    }

    fun getFloodgate(): HookFloodgate {
        return registry[5] as HookFloodgate
    }

    fun getVault(): HookVault {
        return registry[6] as HookVault
    }

    fun getFastScript(): HookFastScript {
        return registry[7] as HookFastScript
    }

    fun getOptEco(): HookOptEco {
        return registry[8] as HookOptEco
    }

}