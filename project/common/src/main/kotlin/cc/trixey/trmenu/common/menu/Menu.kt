package cc.trixey.trmenu.common.menu

import cc.trixey.invero.common.Viewer
import cc.trixey.trmenu.common.animation.CycleMode

/**
 * TrMenu
 * cc.trixey.trmenu.common.menu.Menu
 *
 * @author Arasple
 * @since 2023/1/13 12:49
 */
interface Menu {

    /**
     * The title of this menu
     */
    val title: Array<String>

    /**
     * The update frequency of title
     */
    val titleUpdateInterval: Int

    /**
     * The cycle mode of title animation
     */
    val titleLoopMode: CycleMode

    fun open(viewer: Viewer)

}