package trplugins.menu.api.action.impl.func

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session
import trplugins.menu.util.bukkit.ItemMatcher

/**
 * TrMenu
 * trplugins.menu.api.action.impl.func.GiveItem
 *
 * @author Rubenicos
 * @since 2022/02/14 13:16
 */
class GiveItem(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(give|add)(-)?items?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        ItemMatcher.eval(contents.stringContent().parseContent(placeholderPlayer), !isParsable(contents.stringContent())).buildItem().forEach {
            player.cast<Player>().inventory.addItem(it).values.forEach { e -> player.cast<Player>().world.dropItem(player.cast<Player>().location, e) }
        }
        player.session().playerItemSlots()
    }

}