package cc.trixey.mc.trmenu

import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.Plugin

/**
 * @author Arasple
 * @since 2022/10/28 17:06
 */
@PlatformSide([Platform.BUKKIT])
object TrMenuBukkit : Plugin(), PlatformBridge {

    override fun onLoad() {
        PlatformFactory.registerAPI<PlatformBridge>(this)
    }

    override fun onEnable() {

    }

}