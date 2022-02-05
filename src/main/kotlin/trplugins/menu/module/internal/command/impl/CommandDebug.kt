package trplugins.menu.module.internal.command.impl

import trplugins.menu.TrMenu
import trplugins.menu.api.TrMenuAPI
import trplugins.menu.module.display.Menu
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.display.texture.Texture
import trplugins.menu.module.internal.command.CommandExpression
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.service.Performance
import trplugins.menu.util.Time
import trplugins.menu.util.bukkit.Heads
import trplugins.menu.util.net.Paster
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.submit
import taboolib.module.chat.HexColor
import java.io.File

/**
 * @author Arasple
 * @date 2021/1/28 15:50
 */
object CommandDebug : CommandExpression {

    // trm debug <...>
    override val command = subCommand {
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

            execute<CommandSender> { sender, context, argument ->
                if (argument.isEmpty()) {
                    return@execute
                }

                when (context.argument(0).lowercase()) {
                    "mirror" -> mirror(sender)
                    "dump" -> dump(sender)
                    "info" -> info(sender)
                    "player" -> {
                        val player = when {
                            argument.contains(" ") -> Bukkit.getPlayerExact(argument.substringAfter(" "))
                            sender is Player -> sender
                            else -> null
                        }
                        if (player != null && player.isOnline) {
                            player(sender, player)
                        }
                    }
                    "menu" -> {
                        val menu = when {
                            argument.contains(" ") -> TrMenuAPI.getMenuById(argument.substringAfter(" "))
                            else -> null
                        }
                        if (menu != null) {
                            menu(sender, menu)
                        }
                    }
                    "parsetexture" -> {
                        sender.send("&8[&7Texture&8] ${Texture.createTexture(if (argument.contains(" ")) argument.substringAfter(" ") else "AIR")}")
                    }
                }
            }

        }
    }

    /**
     * 性能损害
     */
    private fun mirror(sender: CommandSender) {
        submit(async = true) {
            val proxySender = adaptCommandSender(sender)
            Performance.collect(proxySender) {
                childFormat = "§8  {0}§7{1} §2[{3} ms] §7{4}%"
                parentFormat = "§8  §8{0}§7{1} §8[{3} ms] §7{4}%"
            }.run {
                sender.sendMessage("\n§b§lTrMenu §a§l§nPerformance Mirror\n§r")
                print(proxySender, getTotal(), 0)
            }
        }
    }

    /**
     * 上传调试信息
     */
    private fun dump(sender: CommandSender) {
        val properties = System.getProperties()
        val dump = buildString {
            append("TrMenu Dump Information (Date: ${Time.formatDate()})\n\n")
            append("| Server OS: ${properties["os.name"]} ${properties["os.arch"]} ${properties["os.version"]}\n")
            append("| Server software: ${Bukkit.getServer().version} (${Bukkit.getServer().bukkitVersion})\n")
            append("| Java version: ${System.getProperty("java.version")}\n\n")
            append("| TrMenu: ${pluginVersion}\n")
//            append("   ${description.getString("built-time")} by ${description.getString("built-by")})\n\n")
            append("Installed Plugins: \n")
            Bukkit.getPluginManager().plugins.sortedBy { it.name }.forEach { plugin ->
                var file: File? = null
                try {
                    Class.forName("org.bukkit.plugin.java.JavaPlugin").also {
                        file = it.getMethod("getFile").also { it.isAccessible = true }.invoke(plugin) as File
                    }
                } catch (t: Throwable) {
                }
                val size = (file?.length() ?: 0) / 1024
                append("· ${plugin.name} - ${plugin.description.version} ($size KB)\n")
            }
        }
        Paster.paste(adaptCommandSender(sender), dump)
    }

    /**
     * 服务器信息查看
     */
    private fun info(sender: CommandSender) {
        val totalTasks = Bukkit.getScheduler().activeWorkers.count { it.owner === TrMenu.plugin } +
                Bukkit.getScheduler().pendingTasks.count { it.owner === TrMenu.plugin }

        sender.send(
            """
                &3&l「&8--------------------------------------------------&3&l」
                
                &2Server: &6${Bukkit.getServer().name}
                &2Plugins: &6${Bukkit.getPluginManager().plugins.size}
                
                &2Total Menus: &6${Menu.menus.size}
                &2Total Tasks: &6$totalTasks
                &2Cached Skulls: &6${Heads.cacheSize()}
                
                &3&l「&8--------------------------------------------------&3&l」
            """.trimIndent().split("\n")
        )
    }

    /**
     * 玩家信息查看
     */
    private fun player(sender: CommandSender, player: Player) {

        val session = MenuSession.getSession(player)

        sender.send(
            """
                &a&l「&8--------------------------------------------------&a&l」
                
                &eSession ID: &6${session.id}
                &ePlaceholder Agent: &6${session.placeholderPlayer.name}
                &eViewing Menu: &6${session.menu?.id} &8(Page: ${session.page})
                &eActive Icons: &6${session.activeIcons.joinToString { it.id }}
                
                &eArguments: &6${session.arguments.contentToString()}
                &eMeta: &6${Metadata.getMeta(session)}
                &eData: &6${Metadata.getData(session)}
                
                &a&l「&8--------------------------------------------------&a&l」
            """.trimIndent().split("\n")
        )
    }

    /**
     * 菜单信息查看
     */
    private fun menu(sender: CommandSender, menu: Menu) {
        sender.send(
            """
                &a&l「&8--------------------------------------------------&a&l」
                
                &eMenu ID: &6${menu.id}
                &eViewers: &7${menu.viewers}
                &eSettings:
                    &6Title: &f${menu.settings.title} &7at ${menu.settings.titleUpdate}ticks
                    &6Arguments: &a${menu.settings.enableArguments} &7${menu.settings.defaultArguments.contentToString()}
                    &6Layout: ${menu.layout.getSize()} &8(Def: ${menu.settings.defaultLayout})
                    &6MinCD: ${menu.settings.minClickDelay}
                    &6HidePINV: ${menu.settings.hidePlayerInventory}
                    &6Bounds: ${menu.settings.boundCommands.joinToString(", ")} / ${menu.settings.boundItems.contentToString()}
                &eIcons:
                    ${menu.icons.joinToString { "&7[ &f${it.id} &7] &8${it.update} ;" }}
                    
                &a&l「&8--------------------------------------------------&a&l」
            """.trimIndent().split("\n")
        )
    }

    /**
     * PRIVATE FUNCS & FIELDS
     */

    private fun CommandSender.send(message: String) {
        sendMessage(HexColor.translate(message))
    }

    private fun CommandSender.send(messages: List<String>) {
        messages.forEach { send(it) }
    }

}