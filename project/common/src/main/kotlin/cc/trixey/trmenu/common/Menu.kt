package cc.trixey.trmenu.common

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.WindowType
import cc.trixey.trmenu.common.animation.CycleMode

/**
 * TrMenu
 * cc.trixey.trmenu.common.Menu
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

    /**
     * The container type this menu presents
     */
    val type: WindowType

    /**
     * The pages of this menu
     */
    val layout: List<Layout>

    /**
     * Icons of this menu
     */
    val icons: Set<Icon>

    /**
     * Options of this menu
     */
    val options: MenuOptions

    /**
     * Bindings of this menu
     */
    val bindings: Set<MenuBinding>

    fun open(viewer: Viewer)

    fun close(viewer: Viewer)

}