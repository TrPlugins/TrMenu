package com.github.username

import taboolib.common.platform.Plugin

/**
 * ExampleProject
 * com.github.username.BukkitPlugin
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
object BukkitPlugin : Plugin() {

    override fun onEnable() {
        Common.info("BukkitPlugin is enabled!")
    }
}