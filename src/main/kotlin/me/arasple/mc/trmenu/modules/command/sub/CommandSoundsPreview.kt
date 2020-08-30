package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.api.factory.MenuFactory
import me.arasple.mc.trmenu.api.factory.task.ClickTask
import me.arasple.mc.trmenu.api.inventory.InvClickType.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/28 11:02
 */
class CommandSoundsPreview : BaseSubCommand() {

    override fun getArguments() = arrayOf(
            Argument("Filter", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        open(sender as Player, 1, if (args.isNotEmpty()) args[0] else null)
    }

    private fun open(player: Player, page: Int, filter: String?) {
        val map = mutableMapOf<Int, Sound>()
        val has = booleanArrayOf(false, false)

        MenuFactory()
                .title("Sounds " + ((if (Strings.nonEmpty(filter)) "[$filter] " else " ") + "(" + page + ")"))
                .layout(
                        "$########",
                        "         ",
                        "         ",
                        "         ",
                        "         ",
                        "#########"
                )
                .item('#', ItemBuilder(Materials.GRAY_STAINED_GLASS_PANE.parseItem()).name("§7Tr§fMenu §3Sounds").build())
                .item('$', ItemBuilder(Materials.GREEN_STAINED_GLASS_PANE.parseItem()).name("§2Stop playing sounds").build())
                .click {
                    if (it.key == "$") {
                        Sounds.stopMusic(player)
                    } else if (has[0] && it.slot == 53) {
                        open(player, page + 1, filter)
                    } else if (has[1] && it.slot == 45) {
                        open(player, page - 1, filter)
                    } else {
                        val sound = map[it.slot]
                        val type = it.type

                        if (sound != null) {
                            when (type) {
                                DROP -> player.playSound(player.location, sound, 1f, 0f)
                                LEFT -> player.playSound(player.location, sound, 1f, 1f)
                                RIGHT -> player.playSound(player.location, sound, 1f, 2f)
                                MIDDLE -> TellrawJson.create().newLine().append("§8▶ §7CLICK TO COPY: ").append("§a§n" + sound.name).clickSuggest(sound.name).hoverText("§8Click this text").newLine().send(player)
                                else -> {
                                }
                            }
                        }
                    }
                    return@click ClickTask.Action.CANCEL_MODIFY
                }
                .build {

                    val sounds = Sound.values().filter { s -> Strings.isBlank(filter) || s.name.toLowerCase().contains(filter!!, true) }.let { list ->
                        return@let list.subList(36 * (page - 1), list.size)
                    }

                    has[0] = sounds.size > 36
                    has[1] = page > 1

                    for (i in 9..44) {
                        if (i >= sounds.size) break
                        map[i] = sounds[i]
                        it.session.setItem(
                                i,
                                ItemBuilder(Material.PAPER)
                                        .name("§f§n" + sounds[i].name)
                                        .lore(
                                                "",
                                                "§8· §7Press §fQ §7for Pitch 0",
                                                "§8· §7Left Click for Pitch 1",
                                                "§8· §7Right Click for Pitch 2",
                                                "",
                                                "§e▶ §6Middle-click to copy the name."
                                        )
                                        .build()
                        )
                    }

                    if (has[0]) {
                        it.session.setItem(53, ItemBuilder(Materials.LIME_STAINED_GLASS_PANE.parseItem()).name("§aNext Page").build())
                    }
                    if (has[1]) {
                        it.session.setItem(45, ItemBuilder(Materials.CYAN_STAINED_GLASS_PANE.parseItem()).name("§3Previous Page").build())
                    }
                }
                .display(player)
    }


}