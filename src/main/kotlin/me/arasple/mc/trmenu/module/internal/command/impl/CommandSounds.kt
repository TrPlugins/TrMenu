package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/1 17:39
 */
class CommandSounds : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Filter", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        open(sender as Player, 0, args.getOrNull(0))
    }

    private fun open(player: Player, page: Int, filter: String?) {
        val sounds = Sound.values().filter { filter == null || it.name.contains(filter, true) }.sorted().let {
            it.subList(54 * page, it.size)
        }

        val prevNext = arrayOf(page > 0, sounds.size > 54)
        val receptacle = InventoryChest(6, "Sounds / Page: $page ; Filter: ${filter ?: "*"}")
        val slotMap = receptacle.type.slotRange
            .mapIndexed { index, slot -> slot to index }
            .filter { it.second < sounds.size }
            .toMap()

        receptacle.setItem(if (prevNext[0]) PREV[0] else PREV[1], prev)
        receptacle.setItem(if (prevNext[1]) NEXT[0] else NEXT[1], next)
        receptacle.setItem(CTRL, ctrl)

        slotMap.forEach { (slot, index) ->
            receptacle.setItem(DISPLAY(sounds[index].name), slot)
        }

        receptacle.listenerClick { _, e ->
            val slot = e.slot
            if (slot < 0) return@listenerClick

            e.isCancelled = true
            receptacle.refresh(e.slot)

            when {
                slot == ctrl -> stopPlaying(player)
                slot == prev && prevNext[0] -> open(player, page - 1, filter)
                slot == next && prevNext[1] -> open(player, page + 1, filter)
                slot in receptacle.type.slotRange -> {
                    val index = slotMap[slot] ?: return@listenerClick
                    val sound = sounds[index]
                    when (e.clickType) {
                        ClickType.DROP -> player.playSound(player.location, sound, 1f, 0f)
                        ClickType.LEFT -> player.playSound(player.location, sound, 1f, 1f)
                        ClickType.RIGHT -> player.playSound(player.location, sound, 1f, 2f)
                        ClickType.MIDDLE -> TellrawJson.create()
                            .newLine()
                            .append("§8▶ §7CLICK TO COPY: ")
                            .append("§a§n" + sound.name).clickSuggest(sound.name)
                            .hoverText("§8Click this text")
                            .newLine()
                            .send(player)
                        else -> {
                        }
                    }
                }
            }
        }

        receptacle.open(player)
    }

    companion object {

        private const val prev = 65
        private const val ctrl = 67
        private const val next = 69

        private val PREV = arrayOf(
            ItemBuilder(Materials.CYAN_STAINED_GLASS_PANE.parseItem()).name("§3Previous Page").build(),
            ItemBuilder(Materials.GRAY_STAINED_GLASS_PANE.parseItem()).name("§7Previous Page").build()
        )

        private val NEXT = arrayOf(
            ItemBuilder(Materials.LIME_STAINED_GLASS_PANE.parseItem()).name("§aNext Page").build(),
            ItemBuilder(Materials.GRAY_STAINED_GLASS_PANE.parseItem()).name("§7Next Page").build()
        )

        private val CTRL = ItemBuilder(Materials.ORANGE_STAINED_GLASS_PANE.parseItem()).name("§6Stop playing").build()

        private val DISPLAY: (String) -> ItemStack = { name ->
            ItemBuilder(Material.PAPER)
                .name("§f§n$name")
                .lore(
                    "",
                    "§8· §7Press §fQ §7for Pitch 0",
                    "§8· §7Left Click for Pitch 1",
                    "§8· §7Right Click for Pitch 2",
                    "",
                    "§e▶ §6Middle-click to copy the name."
                )
                .build()
        }

        private fun stopPlaying(player: Player) {
            Sound.values().forEach { player.stopSound(it) }
        }

    }

}