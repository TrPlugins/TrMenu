package trplugins.menu.api.action.impl

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/2/2 11:05
 */
class ActionUpdate(content: String, option: ActionOption) : InternalAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val session = player.session()
        val menuId = session.menu?.id // 缓存菜单id避免打开下一个菜单出现图标覆盖

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

    companion object : ActionDesc {

        override val name = "(icon)?-?update".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionUpdate(value.toString(), option)
        }

    }

}