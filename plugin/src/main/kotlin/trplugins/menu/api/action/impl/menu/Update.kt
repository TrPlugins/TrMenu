package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Update
 *
 * @author Score2
 * @since 2022/02/14 11:14
 */
class Update(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(icon)?-?update".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val session = player.session()
        val menuId = session.menu?.id // 缓存菜单id避免打开下一个菜单出现图标覆盖
        val baseContent = contents.stringContent()

        if (baseContent.isBlank() || baseContent.equals("update", true)) {
            session.activeIcons.forEach {
                it.onReset(session)
                it.settingItem(session, it.getProperty(session), menuId)
            }
        } else {
            baseContent.split(";").mapNotNull { session.getIcon(it) }.forEach {
                it.onReset(session)
                it.settingItem(session, it.getProperty(session), menuId)
            }
        }
    }

}