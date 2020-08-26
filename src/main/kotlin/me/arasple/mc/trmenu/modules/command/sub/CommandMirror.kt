package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.book.BookFormatter
import io.izzel.taboolib.util.book.builder.PageBuilder
import io.izzel.taboolib.util.chat.ComponentSerializer
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Bkm016, Arasple
 * @date 2020/8/26 14:54
 */
class CommandMirror : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val bookBuilder = BookFormatter.writtenBook()
        bookBuilder.addPages(
            PageBuilder()
                .add("").newLine()
                .add("").newLine()
                .add("      §l§nTrMenu Premium").newLine()
                .add("").newLine()
                .add("   §8Performance Monitoring").newLine()
                .build()
        )
        Mirror.dataMap.keys.toList().sortedBy { Mirror.get(it).average() }.forEach { k ->
            val v = Mirror.get(k)
            val name = k.substring(k.indexOf(":") + 1)
            bookBuilder.addPages(
                ComponentSerializer.parse(
                    TellrawJson.create().newLine()
                        .append("  §1§l§n${k.split(":")[0]}").newLine()
                        .append("  §1" + toSimple(name)).hoverText(name).newLine()
                        .append("").newLine()
                        .append("  Total §7${if (v.total) v.times else "_"} times").newLine()
                        .append("  Total §7${v.timeTotal} ms").newLine()
                        .append("  Average §7${Numbers.format(v.average())} ms ").append("§4(?)").hoverText("§8Details:\n§fLowest §7${v.lowest} ms\n§fHighest §7${v.highest} ms").newLine()
                        .toRawMessage(sender as Player)
                )
            )
        }
        BookFormatter.forceOpen(sender as Player, bookBuilder.build())
    }

    fun toSimple(source: String): String? {
        return if (source.length > 20) source.substring(0, source.length - (source.length - 10)) + "..." + source.substring(source.length - 7) else source
    }

}