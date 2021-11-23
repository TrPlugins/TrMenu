package me.arasple.mc.trmenu.api.menu

import me.arasple.mc.trmenu.module.conf.prop.SerialzeResult
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import taboolib.library.configuration.MemorySection
import java.io.File

/**
 * @author Arasple
 * @date 2021/1/25 10:11
 */
interface ISerializer {

    fun serializeMenu(file: File): SerialzeResult

    fun serializeSetting(conf: MemorySection): SerialzeResult

    fun serializeLayout(conf: MemorySection): SerialzeResult

    fun serializeIcons(conf: MemorySection, layout: MenuLayout): SerialzeResult

}