package cc.trixey.trmenu.core

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.api.dsl.packetChestWindow
import cc.trixey.invero.common.Viewer
import cc.trixey.trmenu.common.animation.CycleArray
import cc.trixey.trmenu.common.animation.CycleMode
import cc.trixey.trmenu.common.createCyclable
import cc.trixey.trmenu.common.menu.Menu

/**
 * TrMenu
 * cc.trixey.trmenu.core.DefaultMenu
 *
 * @author Arasple
 * @since 2023/1/13 13:08
 */
class DefaultMenu(
    override val title: Array<String>,
    override val titleUpdateInterval: Int,
    override val titleLoopMode: CycleMode
) : Menu {

    override fun open(viewer: Viewer) {
        val session = viewer.getMenuSession()
        val title = title.createCyclable()
        val window = packetChestWindow(6, title.get()) { initializeWindow(this) }

        // submit title task
        if (title is CycleArray && titleUpdateInterval > 0) {
            val interval = titleUpdateInterval.toLong()

            session.launchAsync(delay = interval, period = interval) {
                window.title = title.getAndCycle()
            }
        }

        // open
        window.open(viewer)
    }

    private fun initializeWindow(window: BukkitWindow) {
        window.apply {

        }
    }

}