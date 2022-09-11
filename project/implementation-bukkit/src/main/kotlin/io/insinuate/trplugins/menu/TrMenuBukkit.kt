package io.insinuate.trplugins.menu

import taboolib.common.platform.Plugin

/**
 * ExampleProject
 * com.github.username.BukkitPlugin
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
object TrMenuBukkit : Plugin() {

    override fun onEnable() {
        TrMenu.info("BukkitPlugin is enabled!")
    }
}