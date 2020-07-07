package me.arasple.mc.trmenu.data

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/6 22:06
 */
object MetaPlayer {

    private val playerInventorys = mutableMapOf<UUID, Array<ItemStack?>>()
    private val arguments = mutableMapOf<UUID, Array<String>>()
    private val meta = mutableMapOf<UUID, MutableMap<String, Any>>()

    fun getInventoryContents(player: Player): Array<ItemStack?> = this.playerInventorys.computeIfAbsent(player.uniqueId) { player.inventory.contents.clone() }

    fun updateInventoryContents(player: Player) = mutableListOf<ItemStack?>().let {
        val contents = player.inventory.contents
        for (i in 9..35) it.add(contents[i])
        for (i in 0..8) it.add(contents[i])
        this.playerInventorys[player.uniqueId] = it.toTypedArray()
    }

    fun replaceWithArguments(player: Player, strings: List<String>): List<String> = mutableListOf<String>().let { list ->
        val args = arguments.computeIfAbsent(player.uniqueId) { arrayOf() }
        strings.forEach {
            list.add(Strings.replaceWithOrder(it, args))
        }
        return@let list
    }

    fun replaceWithArguments(player: Player, string: String): String {
        val session = MenuSession.session(player)
        var content = string.replace("{page}", session.page.toString())
        meta.computeIfAbsent(player.uniqueId) { mutableMapOf() }.forEach { if (it.value is String) content = content.replace(it.key, it.value.toString()) }
        session.menu?.settings?.functions?.let { it ->
            val funs = it.internalFunctions
            if (funs.isNotEmpty()) {
                val matcher = InternalFunction.match(string)
                while (matcher.find()) {
                    matcher.group(1).split("_").toTypedArray().let { find ->
                        val function = funs.firstOrNull { it.id == find[0] }
                        if (function != null) content = content.replace(matcher.group(), function.eval(player, ArrayUtils.remove(find, 0)))
                    }
                }
            }
        }
        return Strings.replaceWithOrder(content, *arguments.computeIfAbsent(player.uniqueId) { arrayOf() })
    }

    fun getArguments(player: Player): Array<String> = this.arguments.computeIfAbsent(player.uniqueId) { arrayOf() }

    fun setArguments(player: Player, arguments: Array<String>) {
        this.arguments[player.uniqueId] = filterInput(arguments.toMutableList()).toTypedArray()
        Msger.debug("ARGUMENTS", player.name, getArguments(player))
    }

    fun removeArguments(player: Player) = this.arguments.remove(player.uniqueId)

    fun setMeta(player: Player, key: String, value: Any) = meta.computeIfAbsent(player.uniqueId) { mutableMapOf() }.put(key, value)

    fun getMeta(player: Player, key: String): Any? = meta.computeIfAbsent(player.uniqueId) { mutableMapOf() }[key]

    fun removeMeta(player: Player, key: String) = meta[player.uniqueId]?.remove(key)

    fun resetCache(player: Player) {
        this.playerInventorys.remove(player.uniqueId)
        this.meta.remove(player.uniqueId)
    }

    fun filterInput(string: String): String = string.replace(Regex("(?i)\\(|\\)|;|\"|bukkitServer|player"), "")

    fun filterInput(strings: MutableList<String>): List<String> {
        strings.indices.forEach {
            strings[it] = filterInput(strings[it])
        }
        return strings
    }

    fun completeArguments(player: Player, arguments: Array<String>) {
        if (arguments.isNotEmpty()) {
            if (getArguments(player).isEmpty()) {
                setArguments(player, arguments)
            } else if (getArguments(player).size < arguments.size) {
                val args = getArguments(player).toMutableList()
                for (i in getArguments(player).size until arguments.size) args.add(arguments[i])
                setArguments(player, args.toTypedArray())
            }
        }
    }

}