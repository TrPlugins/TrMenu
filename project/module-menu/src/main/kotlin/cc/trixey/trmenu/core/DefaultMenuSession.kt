package cc.trixey.trmenu.core

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.trmenu.common.Menu
import cc.trixey.trmenu.common.Session
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor

/**
 * TrMenu
 * cc.trixey.trmenu.core.DefaultMenuSession
 *
 * @author Arasple
 * @since 2023/1/13 13:29
 */
class DefaultMenuSession(
    override val viewer: BukkitViewer,
    override var menu: Menu? = null,
    override var viewingWindow: Window? = null
) : Session {

    constructor(viewer: Viewer, viewingMenu: Menu? = null) : this(viewer as BukkitViewer, viewingMenu)

    val tasks = mutableSetOf<PlatformExecutor.PlatformTask>()

    fun closure() {
        tasks.forEach { it.cancel() }

        menu = null
        viewingWindow?.close(viewer)
    }

    fun launch(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: PlatformExecutor.PlatformTask.() -> Unit,
    ) = submit(now, async, delay, period, comment, executor).also { tasks += it }

    fun launchAsync(
        now: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: PlatformExecutor.PlatformTask.() -> Unit,
    ) = launch(now, true, delay, period, comment, executor)

}