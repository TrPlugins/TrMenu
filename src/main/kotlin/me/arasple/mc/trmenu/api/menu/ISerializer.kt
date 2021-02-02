package me.arasple.mc.trmenu.api.menu

import me.arasple.mc.trmenu.module.conf.prop.SerialzeResult
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import java.io.File

/**
 * @author Arasple
 * @date 2021/1/25 10:11
 */
interface ISerializer {

    fun serializeMenu(file: File): SerialzeResult

    fun serializeSetting(conf: Map<String, Any>): SerialzeResult

    fun serializeLayout(conf: Map<String, Any>): SerialzeResult

    fun serializeIcon(conf: Map<String, Any>, layout: MenuLayout): SerialzeResult

}