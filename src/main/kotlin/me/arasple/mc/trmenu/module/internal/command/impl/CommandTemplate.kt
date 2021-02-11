package me.arasple.mc.trmenu.module.internal.command.impl

import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.item.inventory.MenuBuilder
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import me.arasple.mc.trmenu.module.display.texture.Texture
import me.arasple.mc.trmenu.util.Time
import me.arasple.mc.trmenu.util.net.Paster
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
@Deprecated("TO BE IMPROVED")
class CommandTemplate : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(
            Argument("Rows", false) {
                listOf("1", "2", "3", "4", "5", "6")
            }
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = sender as Player
        val rows = (if (args.isNotEmpty()) NumberUtils.toInt(args[0], 5) else 3).coerceAtMost(6)

        MenuBuilder.builder()
            .title("Template#$rows")
            .rows(rows)
            .lockHand(false)
            .close { e ->
                val inventory = e.inventory

                if (inventory.all { Items.isNull(it) }) {
                    TLocale.sendTo(player, "Command.Template.Empty")
                    return@close
                }

                Sounds.BLOCK_NOTE_BLOCK_BIT.play(player, 1f, 0f)
                Paster.paste(player, generate(inventory), "yml")

                inventory.contents.forEach { if (!Items.isNull(it)) CronusUtils.addItem(player, it) }
            }
            .open(player)

    }

    /**
     * 生成模板
     */
    private fun generate(inventory: Inventory): String {
        val rows = inventory.size / 9
        val keys = MenuLayout.commonKeys.iterator()
        val items = collectItems(inventory)
        val layout = Array(rows) { "         " }.toMutableList()
        val layoutPlayerInventory = Array(4) { "         " }.toMutableList()
        val yaml = YamlConfiguration().also { conf ->
            conf.options().header(
                buildString {
                    appendLine()
                    append("Made by TrMenu Template\n")
                    append("Date: ${Time.formatDate()}\n ")
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
        section["material"] = Texture.createTexture(item)
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