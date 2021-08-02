package me.arasple.mc.trmenu.module.internal.command.impl

import taboolib.module.ui.receptacle.ChestInventory
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.subCommand
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XSound
import taboolib.module.chat.TellrawJson
import taboolib.module.ui.receptacle.ReceptacleClickType
import taboolib.platform.type.BukkitPlayer
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @date 2021/2/1 17:39
 */
object CommandSounds : CommandExpresser {

    // menu sounds [sound]
    override val command = subCommand {
        dynamic(optional = true) {
            suggestion<CommandSender> { _, _ ->
                XSound.values().map { it.name }
            }

            execute<CommandSender> { sender, context, argument ->
                open(sender as Player, 0, argument.ifEmpty { "*" })
            }
        }
    }

    private fun open(player: Player, page: Int, filter: String?) {
        val sounds = Sound.values().filter { filter == null || it.name.contains(filter, true) }.sorted().let {
            it.subList(54 * page, it.size)
        }

        val prevNext = arrayOf(page > 0, sounds.size > 54)
        val receptacle = ChestInventory(6, "Sounds / Page: $page ; Filter: ${filter ?: "*"}")
        val slotMap = receptacle.type.containerSlots
            .mapIndexed { index, slot -> slot to index }
            .filter { it.second < sounds.size }
            .toMap()

        receptacle.setItem(if (prevNext[0]) PREV[0] else PREV[1], prev)
        receptacle.setItem(if (prevNext[1]) NEXT[0] else NEXT[1], next)
        receptacle.setItem(CTRL, ctrl)

        slotMap.forEach { (slot, index) ->
            receptacle.setItem(DISPLAY(sounds[index].name), slot)
        }

        receptacle.onClick { _, e ->
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
                    when (e.receptacleClickType) {
                        ReceptacleClickType.DROP -> player.playSound(player.location, sound, 1f, 0f)
                        ReceptacleClickType.LEFT -> player.playSound(player.location, sound, 1f, 1f)
                        ReceptacleClickType.RIGHT -> player.playSound(player.location, sound, 1f, 2f)
                        ReceptacleClickType.MIDDLE -> TellrawJson()
                            .newLine()
                            .append("§8▶ §7CLICK TO COPY: ")
                            .append("§a§n" + sound.name).suggestCommand(sound.name)
                            .hoverText("§8Click this text")
                            .newLine()
                            .sendTo(BukkitPlayer(player))
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
        ItemBuilder(XMaterial.CYAN_STAINED_GLASS_PANE).also {
            it.name = "§3Previous Page"
        }.build(),
        ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).also {
            it.name = "§7Previous Page"
        }.build()
    )

    private val NEXT = arrayOf(
        ItemBuilder(XMaterial.LIME_STAINED_GLASS_PANE).also {
            it.name = "§aNext Page"
        }.build(),
        ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE).also {
            it.name = "§7Next Page"
        }.build()
    )

    private val CTRL = ItemBuilder(XMaterial.ORANGE_STAINED_GLASS_PANE).also {
        it.name = "§6Stop playing"
    }.build()

    private val DISPLAY: (String) -> ItemStack = { name ->
        ItemBuilder(XMaterial.PAPER).also {
            it.name = "§f§n$name"
            it.lore.addAll(listOf(
                    "",
                    "§8· §7Press §fQ §7for Pitch 0",
                    "§8· §7Left Click for Pitch 1",
                    "§8· §7Right Click for Pitch 2",
                    "",
                    "§e▶ §6Middle-click to copy the name."
                )
            )
        }.build()
    }

    private fun stopPlaying(player: Player) {
        Sound.values().forEach { player.stopSound(it) }
    }

}