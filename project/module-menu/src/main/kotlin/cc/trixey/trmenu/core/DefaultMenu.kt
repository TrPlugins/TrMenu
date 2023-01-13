package cc.trixey.trmenu.core

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.api.dsl.packetChestWindow
import cc.trixey.invero.bukkit.api.dsl.pagedNetesed
import cc.trixey.invero.bukkit.api.dsl.standard
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.WindowType
import cc.trixey.trmenu.common.*
import cc.trixey.trmenu.common.animation.CycleArray
import cc.trixey.trmenu.common.animation.CycleMode

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
    override val titleLoopMode: CycleMode,
    override val type: WindowType,
    override val layout: List<Layout>,
    override val icons: Set<Icon>,
    override val options: DefaultMenuOptions,
    override val bindings: Set<MenuBinding>,
) : Menu {

    override fun open(viewer: Viewer) {


        val session = viewer.getMenuSession().also { it.closure() }
        val title = title.createCyclable(titleLoopMode)
        val window = packetChestWindow(6, title.get()) { initializeWindow(this) }

        // create title task
        if (title is CycleArray && titleUpdateInterval > 0) {
            val interval = titleUpdateInterval.toLong()

            session.launchAsync(delay = interval, period = interval) {
                window.title = title.getAndCycle()
            }
        }

        // open window
        window.open(viewer)
    }

    override fun close(viewer: Viewer) {
        viewer.getMenuSession().closure()
    }

    private fun initializeWindow(window: BukkitWindow) {
        window.apply {
            if (layout.size == 1) {

                standard {

                }

            } else {

                pagedNetesed {
                    repeat(layout.size) {
                        standard {

                        }
                    }
                }

            }
        }
    }

}