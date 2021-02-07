package me.arasple.mc.trmenu.module.internal.service

import io.izzel.taboolib.kotlin.Mirror

/**
 * @author Arasple
 * @date 2021/2/6 19:31
 */
object Performance {

    fun collect(opt: Mirror.Options.() -> Unit = {}): Mirror.MirrorCollect {
        return MIRROR.collect(opt)
    }

    val MIRROR = Mirror()

}