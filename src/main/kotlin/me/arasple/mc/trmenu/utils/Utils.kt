package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.internal.gson.JsonParser


/**
 * @author Arasple
 * @date 2020/5/30 12:26
 */
object Utils {

    @Suppress("DEPRECATION")
    fun isJson(string: String): Boolean = try {
        JsonParser().parse(string); true
    } catch (e: Throwable) {
        false
    }

}