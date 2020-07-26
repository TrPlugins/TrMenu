package me.arasple.mc.trmenu.data

import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.display.menu.MenuLayout
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

    fun Player.replaceWithArguments(strings: List<String>): List<String> = mutableListOf<String>().let { list ->
        strings.forEach {
            list.add(this.replaceWithArguments(it))
        }
        return@let list
    }

    fun Player.replaceWithArguments(string: String): String {
        val session = this.getMenuSession()
        var content = replaceWithMeta(string.replace("{page}", session.page.toString()))
        session.menu?.settings?.functions?.let { it ->
            content = InternalFunction.replaceWithFunctions(this, it.internalFunctions, content)
        }
        return Strings.replaceWithOrder(content, *this.getArguments())
    }

    fun Player.getArguments() = arguments.computeIfAbsent(this.uniqueId) { arrayOf() }

    fun Player.setArguments(arguments: Array<String>) {
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

    fun Player.replaceWithMeta(string: String): String {
        var content = string
        this.getMeta().forEach {
            content = content.replace(it.key, it.value.toString())
        }
        return content
    }

    fun Player.resetCache() {
        playerInventorys.remove(this.uniqueId)
        meta.remove(this.uniqueId)
    }

    fun Player.completeArguments(arguments: Array<String>) {
        if (arguments.isNotEmpty()) {
            val currentArgs = this.getArguments()
            if (currentArgs.isEmpty()) {
                this.setArguments(currentArgs)
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