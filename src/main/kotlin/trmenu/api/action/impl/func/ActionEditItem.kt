package trmenu.api.action.impl.func

import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.util.Regexs
import trmenu.util.bukkit.ItemHelper
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import taboolib.common.util.replaceWithOrder
import taboolib.library.xseries.XMaterial
import trmenu.api.action.base.ActionDesc

/**
 * @author Rubenicos
 * @date 2021/11/14 19:45
 */
class ActionEditItem(
    val method: Int,
    val item: String,
    val part: Pair<Int, String>,
    val value: List<String>,
    option: ActionOption
) : AbstractAction(option = option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        if (method < 1 || item.isBlank() || part.first < 1 || value.isEmpty()) return
        val item = ItemHelper.fromPlayerInv(player.inventory, this.item) ?: return
        val value = placeholderPlayer.session().parse(this.value)
        when (part.first) {
            1 -> { // Material
                if (method == 1) {
                    val material = XMaterial.matchXMaterial(value[0])
                    if (material.isPresent) {
                        val mat = material.get().parseMaterial() ?: return
                        if (item is Array<*>) {
                            item.forEach { (it as ItemStack?)?.let { item -> material(item, mat) } }
                        } else if (item is ItemStack) material(item, mat)
                    }
                }
            }
            2 -> { // Name
                val name = if (method == 2) null else value.joinToString(" ")
                if (item is Array<*>) {
                    item.forEach { (it as ItemStack?)?.let { item -> name(item, name) } }
                } else if (item is ItemStack) name(item, name)
            }
            3 -> { // Lore
                if (item is Array<*>) {
                    item.forEach { (it as ItemStack?)?.let { item -> lore(item, value) } }
                } else if (item is ItemStack) lore(item, value)
            }
            4 -> { // Flags
                val flags: MutableList<ItemFlag> = ArrayList()
                value.forEach {
                    try {
                        flags.add(ItemFlag.valueOf(it))
                    } catch (ignored: Exception) { }
                }
                if (flags.isNotEmpty()) {
                    if (item is Array<*>) {
                        item.forEach { (it as ItemStack?)?.let { item -> flags(item, flags) } }
                    } else if (item is ItemStack) flags(item, flags)
                }
            }
        }
    }

    private fun material(item: ItemStack, material: Material) {
        item.type = material
    }

    private fun name(item: ItemStack, name: String?) {
        val meta = item.itemMeta?: return
        if (name == null) {
            meta.setDisplayName(null)
        } else {
            meta.setDisplayName((if (method == 3 && meta.hasDisplayName()) meta.displayName else "") + name)
        }
        item.itemMeta = meta
    }

    private fun lore(item: ItemStack, value: List<String>) {
        val meta = item.itemMeta?: return
        var lore = meta.lore?: arrayListOf<String>()
        if (part.second.isBlank()) {
            when (method) {
                1 -> lore = value
                2 -> lore.clear()
                3 -> lore.addAll(value)
            }
        } else {
            val index = if (part.second == "last") lore.size - 1 else part.second.toIntOrNull()?: 0
            if (lore.size >= index) {
                when (method) {
                    1 -> lore[index] = value[0]
                    2 -> lore.removeAt(index)
                    3 -> lore.addAll(index, value)
                }
            } else return
        }
        meta.lore = lore
        item.itemMeta = meta
    }

    private fun flags(item: ItemStack, flags: List<ItemFlag>) {
        val meta = item.itemMeta?: return
        when (method) {
            1 -> {
                meta.itemFlags.forEach { meta.removeItemFlags(it) }
                flags.forEach { meta.addItemFlags(it) }
            }
            2 -> flags.forEach { if (meta.hasItemFlag(it)) meta.removeItemFlags(it) }
            3 -> flags.forEach { if (!meta.hasItemFlag(it)) meta.addItemFlags(it) }
        }
        item.itemMeta = meta
    }

    companion object : ActionDesc {
        override val name = "edit(-)?items?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            val content: String = value.toString()
            val split = content.split(" ", limit = 4)
            var rawValue = split.getOrElse(3) { "" }

            val replacements = Regexs.SENTENCE.findAll(rawValue).mapIndexed { index, result ->
                rawValue = rawValue.replace(result.value, "{$index}")
                index to result.groupValues[1].replace("\\s", " ")
            }.toMap().values.toTypedArray()

            val replacedValue: MutableList<String> = ArrayList()
            rawValue.split(" ").toTypedArray().forEach { s -> replacedValue.add(s.replaceWithOrder(*replacements)) }

            val part = split.getOrElse(2) { "" }.lowercase().split(":", ";", "=", limit = 2)
            ActionEditItem(
                when (split.getOrElse(0) { "" }.lowercase()) {
                    "set", "put" -> 1
                    "delete", "remove" -> 2
                    "add", "concat" -> 3
                    else -> 0
                },
                split.getOrElse(1) { "" },
                when (part.getOrElse(0) { "" }) {
                    "mat", "material", "type" -> 1
                    "name", "displayname" -> 2
                    "lore", "description" -> 3
                    "flag", "flags" -> 4
                    else -> 0
                } to part.getOrElse(1) { "" },
                replacedValue,
                option
            )
        }

    }
}