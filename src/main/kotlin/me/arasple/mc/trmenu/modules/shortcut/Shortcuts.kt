package me.arasple.mc.trmenu.modules.shortcut

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.configuration.serialize.ReactionSerializer
import me.arasple.mc.trmenu.data.MetaPlayer.setArguments
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/28 15:34
 */
object Shortcuts {

    var reactions = arrayOf<Reactions>()
    val sneaking = mutableMapOf<UUID, Long>()

    fun load() {
        Tasks.task(true) {
            reactions = arrayOf(
                serialize("Offhand"),
                serialize("Sneaking-Offhand"),
                serialize("Right-Click-Player"),
                serialize("Sneaking-Right-Click-Player"),
                serialize("PlayerInventory-Border-Left"),
                serialize("PlayerInventory-Border-Right"),
                serialize("PlayerInventory-Border-Middle")
            )
        }
    }

    fun serialize(type: String) = ReactionSerializer.serializeReactions(TrMenu.SETTINGS.get("Shortcuts.$type"))

    fun borderClick(player: Player, clickType: ClickType) {
        val index = if (clickType.isLeftClick) 4 else if (clickType.isRightClick) 5 else 6
        reactions[index].eval(player)
    }

    fun offhand(player: Player): Boolean {
        val sneaking = player.isSneaking
        val index = if (sneaking && (System.currentTimeMillis() - getSneaking(player)) <= 1000) 1 else 0
        return reactions[index].let {
            it.eval(player)
            !it.isEmpty
        }
    }

    fun rightClickPlayer(player: Player, clicked: Player): Boolean {
        reactions[if (player.isSneaking) 3 else 2].let {
            if (!it.isEmpty) {
                player.setArguments(arrayOf(clicked.name))
                it.eval(player)
                return true
            }
        }
        return false
    }

    fun setSneaking(player: Player) {
        sneaking[player.uniqueId] = System.currentTimeMillis()
    }

    fun getSneaking(player: Player) = sneaking.computeIfAbsent(player.uniqueId) { System.currentTimeMillis() }

}