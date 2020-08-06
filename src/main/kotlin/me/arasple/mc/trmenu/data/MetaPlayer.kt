package me.arasple.mc.trmenu.data

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.modules.script.Scripts
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

    fun Player.getInventoryContents(): Array<ItemStack?> = playerInventorys.computeIfAbsent(this.uniqueId) { this.inventory.contents.clone() }

    fun Player.updateInventoryContents() = mutableListOf<ItemStack?>().let {
        val contents = this.inventory.contents
        for (i in 9..35) it.add(contents[i])
        for (i in 0..8) it.add(contents[i])
        playerInventorys[this.uniqueId] = it.toTypedArray()
    }

    fun Player.replaceWithArguments(string: String): String {
        try {
            val session = this.getMenuSession()
            val functions = session.menu?.settings?.functions?.internalFunctions
            val argumented = Strings.replaceWithOrder(string, *this.getArguments())
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
                        m.appendReplacement(buffer, it.eval(this, ArrayUtils.remove(split, 0)))
                    }
                    // Global Js
                    if (group.startsWith("js:")) {
                        m.appendReplacement(buffer, Scripts.expression(this, group.removePrefix("js:")).asString())
                    }
                }
                m.appendTail(buffer).toString()
            }
            // Meta
            this.getMeta().forEach {
                content = content.replace(it.key, it.value.toString())
            }
            return content
        } catch (e: Throwable) {
            Msger.printErrors("ARGUMENT-REPLACE", e, string)
            return string
        }
    }

    fun Player.getArguments() = arguments.computeIfAbsent(this.uniqueId) { arrayOf() }

    fun Player.setArguments(arguments: Array<String>?) {
        if (arguments == null) {
            removeArguments()
            return
        }
        this@MetaPlayer.arguments[this.uniqueId] = filterInput(formatArguments(arguments)).toTypedArray()
        Msger.debug("ARGUMENTS", this.name, this.getArguments().joinToString(","))
        Msger.debug(this, "ARGUMENTS", this.name, this.getArguments().joinToString(","))
    }

    fun Player.removeArguments() = arguments.remove(this.uniqueId)

    fun Player.setMeta(key: String, value: Any) = this.getMeta().put(key, value)

    fun Player.getMeta(key: String) = this.getMeta()[key]

    fun Player.getMeta() = meta.computeIfAbsent(this.uniqueId) { mutableMapOf() }

    fun Player.removeMeta(key: String) = this.getMeta().remove(key)

    fun Player.removeMetaStartsWith(key: String) = this.getMeta().entries.removeIf { it.key.startsWith(key) }

    fun Player.removeMetaEndWith(key: String) = this.getMeta().entries.removeIf { it.key.endsWith(key) }

    fun Player.resetCache() {
        playerInventorys.remove(this.uniqueId)
        meta.remove(this.uniqueId)
    }

    fun Player.completeArguments(arguments: Array<String>) {
        if (arguments.isNotEmpty()) {
            val currentArgs = this.getArguments()
            if (currentArgs.isEmpty()) {
                this.setArguments(arguments)
            } else if (currentArgs.size < currentArgs.size) {
                val args = currentArgs.toMutableList()
                for (i in args.size until currentArgs.size) args.add(currentArgs[i])
                this.setArguments(args.toTypedArray())
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

}