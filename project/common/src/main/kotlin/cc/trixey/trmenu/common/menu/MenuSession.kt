package cc.trixey.trmenu.common.menu

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window

/**
 * TrMenu
 * cc.trixey.trmenu.common.menu.MenuSession
 *
 * @author Arasple
 * @since 2023/1/13 12:53
 */
interface MenuSession {

    val viewer: Viewer

    val menu: Menu?

    val viewingWindow: Window?

    fun isViewing(): Boolean {
        return menu != null || viewingWindow != null
    }

}