package cc.trixey.trmenu.core

import cc.trixey.invero.common.Viewer

/**
 * TrMenu
 * cc.trixey.trmenu.core.Util
 *
 * @author Arasple
 * @since 2023/1/13 13:07
 */

fun Viewer.getMenuSession(): DefaultMenuSession {
    return TrMenuAPI.getSession(this)
}