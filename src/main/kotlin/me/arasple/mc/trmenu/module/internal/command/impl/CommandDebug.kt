package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.TabooLib
import io.izzel.taboolib.kotlin.Mirror
import io.izzel.taboolib.loader.PluginHandle
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.chatcolor.TColor
import io.izzel.taboolib.util.plugin.PluginUtils
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.Time
import me.arasple.mc.trmenu.util.bukkit.Heads
import me.arasple.mc.trmenu.util.net.Paster
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/28 15:50
 */
class CommandDebug : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Type", false) {
            listOf(
                "mirror",
                "dump",
                "info",
                "player"
            )
        }
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        if (args.isNotEmpty()) {
            when (args[0].toLowerCase()) {
                "mirror" -> mirror(sender)
                "dump" -> dump(sender)
                "info" -> info(sender)
                "player" -> {
                    val player = when {
                        args.size > 1 -> Bukkit.getPlayerExact(args[1])
                        sender is Player -> sender
                        else -> null
                    }

                    if (player != null && player.isOnline) {
                        player(sender, player)
                    }
                }
            }
        }
    }

    /**
     * 性能损害
     */
    private fun mirror(sender: CommandSender) {
        Tasks.task(true) {
            Mirror.collect {
                childFormat = "§8[TrMenu] §8{0}§f{1} §8[{2} ms] §c[{3} ms] §7{4}%"
                parentFormat = "§8[TrMenu] §8{0}§7{1} §8[{2} ms] §c[{3} ms] §7{4}%"
            }.run {
                print(sender, getTotal(), 0)
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
            append("| TabooLib: ${TabooLib.getVersion()}\n")
            append("| TrMenu: ${description.getString("version")}\n")
            append("   ${description.getString("built-time")} by ${description.getString("built-by")})\n\n")
            append("Installed Plugins: \n")
            Bukkit.getPluginManager().plugins.sortedBy { it.name }.forEach {
                val size = PluginUtils.getPluginFile(it).length() / 1024
                append("· ${it.name} - ${it.description.version} ($size KB)\n")
            }
        }
        Paster.paste(sender, dump)
    }

    /**
     * 服务器信息查看
     */
    private fun info(sender: CommandSender) {
        val totalTasks = Bukkit.getScheduler().activeWorkers.filter { it.owner === TrMenu.plugin }.count() +
                Bukkit.getScheduler().pendingTasks.filter { it.owner === TrMenu.plugin }.count()

        sender.send(
            """
                &3&l「&8--------------------------------------------------&3&l」
                
                &2Server: &6${Bukkit.getServer().name}
                &2Plugins: &6${Bukkit.getPluginManager().plugins.size}
                &2TabooLib: &f${TabooLib.getVersion()}
                
                &2Total Menus: &6${Menu.menus.size}
                &2Total Tasks: &6$totalTasks
                &2Cached Skulls: &6${Heads.cacheSize()}
                
                &2Built-Info: &a${description.getString("version")}
                &8( &3${description.getString("built-time")} &6by &b${description.getString("built-by")} &8)

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
                
                &eArguments: &6${session.arguments.joinToString("&f, &6")}
                &eMeta: &6${Metadata.getMeta(session)}
                &eData: &6${Metadata.getData(session)}
                
                &a&l「&8--------------------------------------------------&a&l」
            """.trimIndent().split("\n")
        )
    }

    /**
     * PRIVATE FUNCS & FIELDS
     */

    private fun CommandSender.send(messages: List<String>) {
        messages.forEach {
            sendMessage(TColor.translate(it))
        }
    }

    private val description: YamlConfiguration = PluginHandle.getPluginDescription()

}