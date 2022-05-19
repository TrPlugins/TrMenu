package trplugins.menu.api.action.impl.func

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session
import trplugins.menu.util.bukkit.ItemMatcher

/**
 * TrMenu
 * trplugins.menu.api.action.impl.func.TakeItem
 *
 * @author Rubenicos
 * @since 2022/02/14 13:16
 */
class TakeItem(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(take|remove)(-)?items?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        ItemMatcher.eval(contents.stringContent().parseContent(placeholderPlayer), !isParsable(contents.stringContent())).takeItem(player.cast())
        player.session().playerItemSlots()
    }

}