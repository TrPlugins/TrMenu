package me.arasple.mc.trmenu.module.internal.command

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.module.internal.command.impl.*
import org.bukkit.command.CommandSender
import taboolib.common.platform.*
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.MinecraftVersion


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
@CommandHeader(name = "trmenu", aliases = ["menu", "trm"], permission = "trmenu.access")
object CommandHandler {
    @CommandBody(permission = "test", optional = true/*, description = "Test loaded menus"*/)
    val test = CommandTest.command

    @CommandBody(permission = "trmenu.command.list", optional = true/*, description = "List loaded menus"*/)
    val list = CommandList.command

    @CommandBody(permission = "trmenu.command.open", optional = true/*, description = "Open a menu for player"*/)
    val open = CommandOpen.command

    @CommandBody(permission = "trmenu.command.reload", optional = true/*, description = "Reload all menus"*/)
    val reload = CommandReload.command

    @CommandBody(permission = "trmenu.command.template", optional = true/*, description = "Quick layout menu", type = CommandType.PLAYER*/)
    val template = CommandTemplate.command

    @CommandBody(permission = "trmenu.command.action", optional = true/*, description = "Run actions for test"*/)
    val action = CommandAction.command

    @CommandBody(permission = "trmenu.command.item", optional = true/*, description = "Manipulate items"*/)
    val item = CommandItem.command

    @CommandBody(permission = "trmenu.command.sounds", optional = true/*, description = "Preview & test sounds", type = CommandType.PLAYER*/)
    var sounds = CommandSounds.command

    @CommandBody(permission = "trmenu.command.debug", optional = true/*, description = "Print debug info"*/)
    val debug = CommandDebug.command

    @CommandBody
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            generateMainHelper(sender)
        }
    }

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, argument ->
            if (argument.isEmpty()) {
                generateMainHelper(sender)
                return@execute
            }
            sender.sendMessage("§8[§3Tr§bMenu§8] §cERROR §3| Args §6$argument §3not found.")
            TellrawJson()
                .append("§8[§3Tr§bMenu§8] §bINFO §3| Type ").append("§f/trmenu help")
                .hoverText("§f/trmenu help §8- §7more help...")
                .suggestCommand("/trmenu help")
                .append("§3 for help.")
                .sendTo(adaptCommandSender(sender))
        }
    }


    /**
     * 非常烂的相似度匹配, 为 TabooLib 6 的命令帮助移除而写.
     * 暂时弃坑
     */
     /*
    fun surmiseArs(sender: CommandSender, arg: String) {
        var selected: Pair<String, String>? = null
        javaClass.fields.map {
            val name: String
            val desc: String
            try {
                name = it.name
                (it.get(this) as CommandExpresser).apply {
                    desc = this.description
                }
            } catch (t: Throwable) {
                return@map null
            }
            return@map name to desc
        }.forEach {
            it ?: return@forEach
            val origin = arg.toList()
            val target = it.first.toList()


            origin.intersect(target).size / origin.union(target).size

        }

    }
    */

    fun generateMainHelper(sender: CommandSender) {
        val proxySender = adaptCommandSender(sender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  ").append("§3§lTr§b§lMenu")
            .hoverText("§7TrMenu is modern and advanced Minecraft menu-plugin")
            .append(" ").append("§2${TrMenu.plugin.description.version}")
            .hoverText("""
                §7Plugin version: §2${TrMenu.plugin.description.version}
                §7NMS version: §b${MinecraftVersion.minecraftVersion}
            """.trimIndent()).sendTo(proxySender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  §7Type: ").append("§f/trmenu §8[...]")
            .hoverText("§f/trmenu §8[...]")
            .suggestCommand("/trmenu ")
            .sendTo(proxySender)
        proxySender.sendMessage("  §7Args:")

        fun displayArg(name: String, desc: String) {
            TellrawJson()
                .append("    §8- ").append("§f$name")
                .hoverText("§f/trmenu $name §8- §7$desc")
                .suggestCommand("/trmenu $name ")
                .sendTo(proxySender)
            proxySender.sendMessage("      §7$desc")
        }
        displayArg("list", CommandList.description)
        displayArg("open", CommandOpen.description)
        displayArg("reload", CommandReload.description)
        displayArg("action", CommandAction.description)
        displayArg("item", CommandItem.description)
        displayArg("template", CommandTemplate.description)
        displayArg("sounds", CommandSounds.description)
        proxySender.sendMessage("")
    }

}