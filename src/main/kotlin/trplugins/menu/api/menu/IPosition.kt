package trplugins.menu.api.menu

import trplugins.menu.module.display.MenuSession

/**
 * @author Arasple
 * @date 2021/1/25 19:17
 */
interface IPosition {

    fun generatePosition(session: MenuSession)

    fun currentPosition(session: MenuSession)

}