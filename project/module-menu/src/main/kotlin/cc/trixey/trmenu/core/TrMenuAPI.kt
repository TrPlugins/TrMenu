package cc.trixey.trmenu.core

import cc.trixey.invero.common.Viewer
import java.util.*

/**
 * TrMenu
 * cc.trixey.trmenu.core.TrMenuAPI
 *
 * @author Arasple
 * @since 2023/1/13 13:34
 */
object TrMenuAPI {

    val sessions = hashMapOf<UUID, DefaultMenuSession>()

    fun getSession(viewer: Viewer): DefaultMenuSession {
        return sessions.computeIfAbsent(viewer.uuid) { DefaultMenuSession(viewer) }
    }

}