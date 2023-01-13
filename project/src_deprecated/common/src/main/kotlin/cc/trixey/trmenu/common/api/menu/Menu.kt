package cc.trixey.trmenu.common.api.menu

import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.Window
import taboolib.common.platform.ProxyPlayer
import java.util.UUID

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.menu.Menu
 *
 * @author Score2
 * @since 2022/12/10 1:02
 */
abstract class Menu(
    val id: String = UUID.randomUUID().toString(),
) {

    abstract val panels: List<Panel>

    abstract fun windowed(viewer: ProxyPlayer): Window

    fun open(viewer: ProxyPlayer): Window {
        return windowed(viewer).apply { this.open() }
    }

}