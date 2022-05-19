package trplugins.menu.api.menu

import trplugins.menu.module.conf.prop.SerialzeResult
import trplugins.menu.module.display.layout.MenuLayout
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * @author Arasple
 * @date 2021/1/25 10:11
 */
interface ISerializer {

    fun serializeMenu(file: File): SerialzeResult

    fun serializeSetting(conf: Configuration): SerialzeResult

    fun serializeLayout(conf: Configuration): SerialzeResult

    fun serializeIcons(conf: Configuration, layout: MenuLayout): SerialzeResult

}