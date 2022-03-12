package trplugins.menu.module.internal.command.impl

import trplugins.menu.module.internal.command.CommandExpression
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XSound
import taboolib.module.lang.asLangText
import taboolib.module.lang.asLangTextList
import taboolib.platform.util.asLangText
import taboolib.platform.util.buildItem
import taboolib.platform.util.sendLang
import trplugins.menu.api.receptacle.vanilla.window.ChestInventory
import trplugins.menu.api.receptacle.ReceptacleClickType

/**
 * @author Arasple
 * @date 2021/2/1 17:39
 */
object CommandSounds : CommandExpression {

    private val playingSounds = mutableMapOf<Player, MutableSet<XSound>>()

    // trm sounds [sound]
    override val command = subCommand {
        dynamic(optional = true) {
            suggestion<Player>(uncheck = true) { _, _ ->
                XSound.values().map { it.name }
            }

            execute<Player> { sender, _, argument ->
                open(sender, 0, argument.ifEmpty { "*" })
            }
        }
        execute<Player> { sender, _, _ ->
            open(sender, 0, null)
        }
    }

    private fun open(player: Player, page: Int, filter: String?) {
        val sounds = XSound.values().filter { filter == null || it.name.contains(filter, true) }.sorted().let {
            it.subList(54 * page, it.size)
        }

        val prevNext = arrayOf(page > 0, sounds.size > 54)
        val receptacle = ChestInventory(6, player.asLangText("Menu-Internal-Sounds-Title", page, filter ?: "*"))
        val slotMap = receptacle.type.containerSlots
            .mapIndexed { index, slot -> slot to index }
            .filter { it.second < sounds.size }
            .toMap()

        receptacle.hidePlayerInventory(true)
        receptacle.setElement(if (prevNext[0]) PREV[0] else PREV[1], prev)
        receptacle.setElement(if (prevNext[1]) NEXT[0] else NEXT[1], next)
        receptacle.setElement(CTRL, ctrl)

        slotMap.forEach { (slot, index) ->
            receptacle.setElement(DISPLAY(sounds[index].name), slot)
        }

        receptacle.onOpen = { player, _ ->
            playingSounds[player] = mutableSetOf()
        }

        receptacle.onClose = { player, _ ->
            stopPlaying(player)
            playingSounds.remove(player)
        }

        receptacle.onClick = onClick@{ _, e ->
            val slot = e.slot
            if (slot < 0) return@onClick

            e.isCancelled = true
            receptacle.refresh(e.slot)

            when {
                slot == ctrl -> stopPlaying(player)
                slot == prev && prevNext[0] -> open(player, page - 1, filter)
                slot == next && prevNext[1] -> open(player, page + 1, filter)
                slot in receptacle.type.containerSlots -> {
                    val index = slotMap[slot] ?: return@onClick
                    val sound = sounds[index]
                    playingSounds[player]?.add(sound)
                    when (e.receptacleClickType) {
                        ReceptacleClickType.DROP -> sound.play(player, 1f, 0f)
                        ReceptacleClickType.LEFT -> sound.play(player, 1f, 1f)
                        ReceptacleClickType.RIGHT -> sound.play(player, 1f, 2f)
                        ReceptacleClickType.MIDDLE -> player.sendLang("Menu-Internal-Sounds-Copy", sound.name)
                        else -> {
                        }
                    }
                }
            }
        }

        receptacle.open(player)
    }

    private const val prev = 65
    private const val ctrl = 67
    private const val next = 69

    private val PREV = arrayOf(
        buildItem(XMaterial.CYAN_STAINED_GLASS_PANE) {
            name = "§3${console().asLangText("Menu-Internal-Page-Previous")}"
        },
        buildItem(XMaterial.GRAY_STAINED_GLASS_PANE) {
            name = "§7${console().asLangText("Menu-Internal-Page-Previous")}"
        }
    )

    private val NEXT = arrayOf(
        buildItem(XMaterial.LIME_STAINED_GLASS_PANE) {
            name = "§a${console().asLangText("Menu-Internal-Page-Next")}"
        },
        buildItem(XMaterial.GRAY_STAINED_GLASS_PANE) {
            name = "§7${console().asLangText("Menu-Internal-Page-Next")}"
        }
    )

    private val CTRL = buildItem(XMaterial.ORANGE_STAINED_GLASS_PANE) {
        name = "§6${console().asLangText("Menu-Internal-Sounds-Stop-Playing")}"
    }

    private val DISPLAY: (String) -> ItemStack = { name ->
        buildItem(XMaterial.PAPER) {
            this.name = "§f§n$name"
            lore.addAll(console().asLangTextList("Menu-Internal-Sounds-Display"))
        }
    }

    private fun stopPlaying(player: Player) {
        playingSounds[player]?.forEach {
            it.stopSound(player)
        }
    }

}