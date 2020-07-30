package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.module.config.TConfigWatcher
import java.io.File


/**
 * @author Arasple
 * @date 2020/7/28 11:43
 */
object FileWatcher {

    fun isListening(file: File) = watcher.hasListener(file)

    fun listener(file: File, runnable: Runnable) = watcher.addSimpleListener(file, runnable)

    val watcher = TConfigWatcher()

}