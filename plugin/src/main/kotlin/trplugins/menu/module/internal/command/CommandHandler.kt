package trplugins.menu.module.internal.command

import trplugins.menu.TrMenu
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.asLangText
import trplugins.menu.module.internal.command.impl.*
import java.util.*


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
@CommandHeader(name = "trmenu", aliases = ["menu", "trm"], permission = "trmenu.access")
object CommandHandler {
    @CommandBody(permission = "test", optional = true)
    val test = CommandTest.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.list", optional = true)
    val list = CommandList.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.open", optional = true)
    val open = CommandOpen.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.reload", optional = true)
    val reload = CommandReload.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.template", optional = true)
    val template = CommandTemplate.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.action", optional = true)
    val action = CommandAction.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.item", optional = true)
    val item = CommandItem.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.convert", optional = true)
    val convert = CommandConvert.command

    @AppearHelper
    @CommandBody(permission = "trmenu.command.sounds", optional = true)
    var sounds = CommandSounds.command

    @CommandBody(permission = "trmenu.command.debug", optional = true)
    val debug = CommandDebug.command

    @CommandBody(optional = true)
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

    fun generateMainHelper(sender: CommandSender) {
        val proxySender = adaptCommandSender(sender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  ").append("§3TrMenu")
            .hoverText("§7TrMenu is modern and advanced Minecraft trplugins.menu-plugin")
            .append(" ").append("§f${TrMenu.plugin.description.version}")
            .hoverText("""
                §7Plugin version: §2${TrMenu.plugin.description.version}
                §7NMS version: §b${MinecraftVersion.minecraftVersion}
            """.trimIndent()).sendTo(proxySender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  §7${sender.asLangText("Command-Help-Type")}: ").append("§f/trmenu §8[...]")
            .hoverText("§f/trmenu §8[...]")
            .suggestCommand("/trmenu ")
            .sendTo(proxySender)
        proxySender.sendMessage("  §7${sender.asLangText("Command-Help-Args")}:")

        javaClass.declaredFields.forEach {
            if (!it.isAnnotationPresent(AppearHelper::class.java)) return@forEach
            val name = it.name
            val regularName = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val desc = sender.asLangText("Command-$regularName-Description")

            TellrawJson()
                .append("    §8- ").append("§f$name")
                .hoverText("§f/trmenu $name §8- §7$desc")
                .suggestCommand("/trmenu $name ")
                .sendTo(proxySender)
            proxySender.sendMessage("      §7$desc")
        }
        proxySender.sendMessage("")
    }

}