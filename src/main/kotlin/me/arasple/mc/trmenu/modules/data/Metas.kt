package me.arasple.mc.trmenu.modules.data

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.modules.display.function.InternalFunction
import me.arasple.mc.trmenu.modules.display.menu.MenuLayout
import me.arasple.mc.trmenu.modules.function.script.Scripts
import me.arasple.mc.trmenu.util.Msger
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/6 22:06
 */
object Metas {

    private val playerInventorys = mutableMapOf<UUID, Array<ItemStack?>>()
    private val arguments = mutableMapOf<UUID, Array<String>>()
    private val meta = mutableMapOf<UUID, MutableMap<String, Any>>()

    fun getInventoryContents(player: Player): Array<ItemStack?> {
        return playerInventorys.computeIfAbsent(player.uniqueId) { player.inventory.contents.clone() }
    }

    fun updateInventoryContents(player: Player) {
        return mutableListOf<ItemStack?>().let {
            val contents = player.inventory.contents
            for (i in 9..35) it.add(contents[i])
            for (i in 0..8) it.add(contents[i])
            playerInventorys[player.uniqueId] = it.toTypedArray()
        }
    }

    fun replaceWithArguments(player: Player, string: String): String {
        try {
            val session = player.getMenuSession()
            val functions = session.menu?.settings?.functions?.internalFunctions
            val argumented = Strings.replaceWithOrder(string, *getArguments(player))
            val buffer = StringBuffer(argumented.length)
            // Js & Placeholders from Menu
            var content = InternalFunction.match(
                argumented
                    .replace("{page}", session.page.toString())
                    .replace("{displayPage}", (session.page + 1).toString())
            ).let { m ->
                while (m.find()) {
                    val group = m.group(1)
                    val split = group.split("_").toTypedArray()
                    // Internal Functions
                    functions?.firstOrNull { it.id.equals(split[0], true) }?.let {
                        m.appendReplacement(buffer, it.eval(player, ArrayUtils.remove(split, 0)))
                    }
                    // Global Js
                    if (group.startsWith("js:")) {
                        m.appendReplacement(buffer, Scripts.expression(player, group.removePrefix("js:")).asString())
                    }
                }
                m.appendTail(buffer).toString()
            }
            // Meta
            getMeta(player).forEach {
                content = content.replace(it.key, it.value.toString())
            }
            return content
        } catch (e: Throwable) {
            Msger.printErrors("ARGUMENT-REPLACE", e, string)
            return string
        }
    }

    fun getArguments(player: Player): Array<String> {
        return arguments.computeIfAbsent(player.uniqueId) { arrayOf() }
    }

    fun setArguments(player: Player, arguments: Array<String>?) {
        if (arguments == null) {
            removeArguments(player)
            return
        }
        this@Metas.arguments[player.uniqueId] = filterInput(formatArguments(arguments)).toTypedArray()
        Msger.debug("ARGUMENTS", player.name, getArguments(player).joinToString(","))
        Msger.debug(player, "ARGUMENTS", player.name, getArguments(player).joinToString(","))
    }

    fun removeArguments(player: Player) {
        arguments.remove(player.uniqueId)
    }

    fun setMeta(player: Player, key: String, value: Any): Any? {
        return getMeta(player).put(key, value)
    }

    fun getMeta(player: Player, key: String): Any? {
        return getMeta(player)[key]
    }

    fun getMeta(player: Player): MutableMap<String, Any> {
        return meta.computeIfAbsent(player.uniqueId) { mutableMapOf() }
    }

    fun removeMeta(player: Player, key: String) {
        getMeta(player).remove(key)
    }

    fun removeMeta(player: Player, matcher: Matcher) {
        getMeta(player).entries.removeIf { matcher.match(it.key) }
    }

    fun removeMetaEndWith(player: Player, key: String) {
        getMeta(player).entries.removeIf { it.key.endsWith(key) }
    }

    fun resetCache(player: Player) {
        playerInventorys.remove(player.uniqueId)
        meta.remove(player.uniqueId)
    }

    fun completeArguments(player: Player, arguments: Array<String>) {
        if (arguments.isNotEmpty()) {
            val currentArgs = getArguments(player)
            if (currentArgs.isEmpty()) {
                setArguments(player, arguments)
            } else if (currentArgs.size < currentArgs.size) {
                val args = currentArgs.toMutableList()
                for (i in args.size until currentArgs.size) args.add(currentArgs[i])
                setArguments(player, args.toTypedArray())
            }
        }
    }

    fun filterInput(string: String): String = string.replace(Regex("(?i)\\(|\\)|;|\"|bukkitServer|player"), "")

    fun filterInput(strings: MutableList<String>): List<String> {
        strings.indices.forEach {
            strings[it] = filterInput(strings[it])
        }
        return strings
    }

    fun formatArguments(arguments: Array<String>) =
        mutableListOf<String>().let { list ->
            Variables(arguments.joinToString(" "), MenuLayout.ICON_KEY_MATCHER).find().variableList.forEach { it ->
                if (it.isVariable) {
                    list.add(it.text)
                } else {
                    list.addAll(it.text.split(" ").filter { it.isNotBlank() })
                }
            }
            list
        }

    fun interface Matcher {

        fun match(key: String): Boolean

    }

}