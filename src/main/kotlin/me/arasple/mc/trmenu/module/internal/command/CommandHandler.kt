package me.arasple.mc.trmenu.module.internal.command

import io.izzel.taboolib.kotlin.Indexed
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.texture.Texture
import me.arasple.mc.trmenu.module.internal.command.impl.*
import me.arasple.mc.trmenu.module.internal.command.impl.CommandDebug.send
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.util.net.Paster
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import taboolib.common.LifeCycle
import taboolib.common.platform.*
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XSound
import taboolib.module.ui.buildMenu
import taboolib.module.ui.receptacle.ChestInventory
import taboolib.module.ui.receptacle.createReceptacle
import taboolib.module.ui.type.Basic
import taboolib.platform.util.sendLang
import taboolib.type.BukkitEquipment
import kotlin.system.measureNanoTime


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
object CommandHandler {

    @Awake(LifeCycle.ENABLE)
    fun register() {
        command("trmenu", aliases = listOf("menu"), permission = "trmenu.access") {
            literal("test", permission = "test") {
                // menu test
                execute<Player> { player, _, _ ->
                    val chest = InventoryType.CHEST.createReceptacle("Def").also {
                        it as ChestInventory
                        it.rows = 3
                    }

                    chest.type.totalSlots.forEach { chest.setItem(XMaterial.values().random().parseItem(), it) }
                    chest.open(player)

                    val task = submit(delay = 20, period = 10, async = false) {
                        chest.title = (0..20).random().toString()
                    }
                    submit(delay = (20 * 20)) {
                        task.cancel()
                    }
                }
            }

            literal("list", permission = "trmenu.command.list") {
                // menu list [filter]
                execute<CommandSender> { sender, context, argument ->
                    val filter = if (context.args.isNotEmpty()) argument else null
                    val menus = Menu.menus.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

                    if (menus.isEmpty()) {
                        sender.sendLang("Command-List-Error", filter ?: "*")
                    } else {
                        sender.sendLang("Command-List-Header", menus.size, filter ?: "*")

                        menus.forEach {
                            sender.sendLang(
                                "Command-List-Format", it.id,
                                it.settings.title.elements.first(),
                                it.icons.size
                            )
                        }
                    }
                }
            }

            literal("open", permission = "trmenu.command.open") {
                // menu open [menuId] [player] [args...]
                dynamic(optional = true) {
                    suggestion<CommandSender> { _, _ ->
                        Menu.menus.map { it.id }
                    }
                    dynamic(optional = true) {
                        suggestion<CommandSender> { _, _ ->
                            Bukkit.getOnlinePlayers().map { it.name }
                        }
                    }
                }
                execute<CommandSender> { sender, context, _ ->
                    val split = context.args[0].split(":")
                    val menu = TrMenuAPI.getMenuById(split[0])
                    val page = split.getOrNull(1)?.toIntOrNull() ?: 0
                    val player = if (context.args.size > 1) Bukkit.getPlayerExact(context.args[1]) else if (sender is Player) sender else null
                    val arguments = if (context.args.size > 2) ArrayUtils.removeAll(context.args, 0, 1) else null

                    if (menu == null) {
                        sender.sendLang("Command-Open-Unknown-Menu", context.args[0])
                        return@execute
                    }
                    if (player == null || !player.isOnline) {
                        sender.sendLang("Command-Open-Unknown-Player", context.args.getOrNull(1) ?: "null")
                        return@execute
                    }

                    menu.open(player, page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
                        if (!Metadata.byBukkit(player, "FORCE_ARGS") || (arguments != null && arguments.isNotEmpty())) {
                            it.arguments = arguments ?: arrayOf()
                        }
                    }
                }
            }

            literal("reload", permission = "trmenu.command.reload") {
                execute<CommandSender> { sender, _, _ ->
                    Loader.loadMenus(sender)
                }
            }

            literal("template", permission = "trmenu.command.template") {
                dynamic(optional = true) {
                    suggestion<CommandSender> { _, _ ->
                        listOf("1", "2", "3", "4", "5", "6")
                    }
                }
                execute<Player> { player, context, argument ->
                    val rows = (if (context.args.isNotEmpty()) NumberUtils.toInt(context.args[0], 5) else 3).coerceAtMost(6)

                    buildMenu<Basic>("Template#$rows") {
                        rows(rows)
                        handLocked(false)
                        onClose { e ->
                            val inventory = e.inventory

                            if (inventory.all { it == null || it.type == Material.AIR }) {
                                player.sendLang("Command-Template-Empty")
                                return@onClose
                            }

                            XSound.BLOCK_NOTE_BLOCK_BIT.play(player, 1f, 0f)
                            Paster.paste(player, CommandTemplate.generate(inventory), "yml")

                            inventory.contents.forEach {
                                if (!(it == null || it.type == Material.AIR)) {
                                    player.inventory.addItem(it).values.forEach { e -> player.world.dropItem(player.location, e) }
                                }
                            }
                        }
                    }
                }
            }


            literal("action", permission = "trmenu.command.action") {
                dynamic(optional = true) {
                    suggestion<CommandSender> { sender, context ->
                        Bukkit.getOnlinePlayers().map { it.name }
                    }
                }
                execute<CommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(context.args[0])

                    if (player == null || !player.isOnline) {
                        sender.sendLang("Command-Action-Unknown-Player", context.args[0])
                        return@execute
                    }

                    Indexed.join(context.args, 1).let { it ->
                        val (hidePrint, action) = it.startsWith("#") to Actions.readAction(it.removePrefix("#"))

                        measureNanoTime {
                            Actions.runAction(player, action).let {
                                sender.sendMessage("§8[§7Action§8] §7Result: §3$it")
                            }
                        }.also {
                            if (!hidePrint) {
                                sender.sendMessage("§8[§7Action§8] §7Evaluated §8{$action} §7in §f${it.div(1000000.0)} ms")
                            }
                        }
                    }
                }
            }
            // toJson -NoValueNeeded-
            // fromJson [value]
            // save [id]
            // get [id]
            // del [id]
            literal("item", permission = "trmenu.command.item") {
                execute<CommandSender> { player, context, _ ->

                    XSound.ITEM_BOTTLE_FILL.play(player as Player, 1f, 0f)

                    val value = context.args.getOrNull(1)
                    val item = BukkitEquipment.getItems(player)[BukkitEquipment.HAND]

                    if (context.args[0].equals("toJson", true)) {
                        item ?: kotlin.run {
                            player.sendLang("Command-Item-No-Item")
                            return@execute
                        }
                        CommandItem.toJson(player, item)
                    } else if (value != null) {
                        when (context.args[0].lowercase()) {
                            "fromjson" -> CommandItem.fromJson(player, value)
                            "get" -> ItemRepository.getItem(value)?.let {
                                player.inventory.addItem(it).values.forEach { e -> player.world.dropItem(player.location, e) }
                            }
                            "save" -> item?.let {
                                ItemRepository.getItemStacks()[value] = item
                                player.sendLang("Command-Item-Saved", value)
                            }
                            "delete" -> ItemRepository.removeItem(value)?.let {
                                player.sendLang("Command-Item-Deleted", value)
                            }
                        }
                    }
                }
            }

            literal("sounds", permission = "trmenu.command.sounds") {
                dynamic(optional = true) {
                    suggestion<CommandSender> { _, _ ->
                        XSound.values().map { it.name }
                    }

                }
                execute<CommandSender> { sender, _, argument ->
                    CommandSounds.open(sender as Player, 0, argument)
                }
            }

            literal("debug", permission = "trmenu.command.debug") {
                dynamic(optional = true) {
                    suggestion<CommandSender> { _, _ ->
                        listOf(
                            "mirror",
                            "dump",
                            "info",
                            "player",
                            "menu",
                            "parseTexture"
                        )
                    }
                }
                execute<CommandSender> { sender, context, argument ->
                    if (context.args.isNotEmpty()) {
                        when (context.args[0].lowercase()) {
                            "mirror" -> CommandDebug.mirror(sender)
                            "dump" -> CommandDebug.dump(sender)
                            "info" -> CommandDebug.info(sender)
                            "player" -> {
                                val player = when {
                                    context.args.size > 1 -> Bukkit.getPlayerExact(context.args[1])
                                    sender is Player -> sender
                                    else -> null
                                }
                                if (player != null && player.isOnline) {
                                    CommandDebug.player(sender, player)
                                }
                            }
                            "menu" -> {
                                val menu = when {
                                    context.args.size > 1 -> TrMenuAPI.getMenuById(context.args[1])
                                    else -> null
                                }
                                if (menu != null) {
                                    CommandDebug.menu(sender, menu)
                                }
                            }
                            "parsetexture" -> {
                                sender.send("&8[&7Texture&8] ${Texture.createTexture(context.args.getOrElse(1) { "AIR" })}")
                            }
                        }
                    }
                }
            }


        }
    }
}