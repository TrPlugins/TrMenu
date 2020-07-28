package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Hastebin
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.item.inventory.MenuBuilder
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.display.item.property.Mat
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.modules.migrate.Migrater
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemoryConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


/**
 * @author Arasple
 * @date 2020/7/22 12:08
 */
class CommandTemplate : BaseSubCommand() {

    override fun getArguments() =
        arrayOf(
            Argument("Rows", false) {
                listOf("1", "2", "3", "4", "5", "6")
            }
        )

    override fun getType() = CommandType.PLAYER

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val player = sender as Player
        val rows = (if (args.isNotEmpty()) NumberUtils.toInt(args[0], 5) else 3).coerceAtMost(6)

        MenuBuilder.builder()
            .title("Template#$rows")
            .rows(rows)
            .close {
                val inventory = it.inventory
                if (isEmpty(inventory)) {
                    TLocale.sendTo(player, "COMMANDS.TEMPLATE.EMPTY")
                    return@close
                }
                Tasks.run(true) {
                    Sounds.BLOCK_NOTE_BLOCK_BIT.play(player, 1f, 0f)
                    TLocale.sendTo(player, "COMMANDS.TEMPLATE.PROCESSING")
                    Hastebin.paste(format(inventory)).url.let { url ->
                        TLocale.sendTo(player, if (url != null) "COMMANDS.TEMPLATE.SUCCESS" else "COMMANDS.TEMPLATE.FAILED", url)
                    }
                }
                inventory.contents.forEach { item ->
                    if (!Items.isNull(item)) CronusUtils.addItem(player, item)
                }
            }
            .lockHand(false)
            .open(player)
    }

    private fun isEmpty(inventory: Inventory): Boolean = inventory.contents.none { !Items.isNull(it) }

    private fun format(inventory: Inventory): String {
        val rows = inventory.size / 9
        val keys = Migrater.keys.iterator()
        val items = collectItems(inventory)
        val layout = MenuLayout.blankLayout(rows)
        val layoutPlayerInventory = MenuLayout.blankLayout(4)
        val yaml = YamlConfiguration().also { conf ->
            conf.options().header(
                buildString {
                    appendLine()
                    append("Made by TrMenu Template\n")
                    append("Date: ${Migrater.getExactDate()}\n ")
                    appendLine()
                }
            )
            conf["Title"] = "Template*$rows"
            conf["Layout"] = layout
            if (items.any { it -> it.value.any { it > inventory.size } }) {
                conf["PlayerInventory"] = layoutPlayerInventory
            }
        }

        items.entries.sortedByDescending { it.value.size }.forEach { (item, slots) ->
            val key = keys.next()
            slots.forEach {
                if (it > inventory.size) modifyLayout(layoutPlayerInventory, it, key)
                else modifyLayout(layout, it, key)
            }
            yaml["Icons.$key.display"] = formatDisplaySection(item)
        }

        return yaml.saveToString()
    }

    private fun collectItems(inventory: Inventory): MutableMap<ItemStack, MutableSet<Int>> {
        val items = mutableMapOf<ItemStack, MutableSet<Int>>()

        for (i in 0 until inventory.size) {
            val item = inventory.getItem(i)
            if (!Items.isNull(item)) {
                items.computeIfAbsent(item!!) { mutableSetOf() }.add(i)
            }
        }
        return items
    }

    private fun formatDisplaySection(item: ItemStack): ConfigurationSection {
        val section = MemoryConfiguration()
        val meta = item.itemMeta
        section["material"] = Mat.createMat(item)
        meta?.let {
            if (it.hasDisplayName()) section["name"] = it.displayName
            if (it.hasLore()) section["lore"] = it.lore
            if (it.itemFlags.isNotEmpty()) section["flags"] = it.itemFlags.map { flag -> flag.name }
        }
        if (item.amount > 1) section["amount"] = item.amount
        if (item.enchantments.isNotEmpty()) section["shiny"] = true
        return section
    }

    private fun modifyLayout(layout: MutableList<String>, slot: Int, key: Char) {
        var line = 0
        var pos = slot + 1
        while (pos > 9) {
            pos -= 9
            line++
        }
        layout[line].toCharArray().let {
            it[pos - 1] = key
            layout[line] = String(it)
        }
    }

}