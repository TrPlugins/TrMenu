package cc.trixey.mc.trmenu

import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.Plugin

/**
 * ExampleProject
 * com.github.username.BukkitPlugin
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
@PlatformSide([Platform.BUKKIT])
object TrMenuBungee : Plugin(), PlatformBridge {

    override fun onLoad() {
        PlatformFactory.registerAPI<PlatformBridge>(this)
    }

    override fun onEnable() {

    }
}