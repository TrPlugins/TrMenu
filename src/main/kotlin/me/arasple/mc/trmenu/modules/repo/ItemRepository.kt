package me.arasple.mc.trmenu.modules.repo

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/7/26 8:53
 */
object ItemRepository {

    private val data = TConfig.create(TrMenu.plugin, "items.yml").listener { load() }
    private var writing = false
    private val itemStacks = mutableMapOf<String, ItemStack>()

    @TSchedule(delay = 20 * 60, period = 20 * 60, async = true)
    fun init() {
        writing = true
        data.getKeys(true).filter { !itemStacks.keys.contains(it) }.forEach {
            data.set(it, null)
        }
        itemStacks.forEach { (id, item) ->
            data.set(id, item)
        }
        Tasks.delay(2) {
            writing = false
        }
    }

    @TSchedule(delay = 20)
    private fun load() {
        if (writing) return
        Tasks.run(true) {
            val keys = data.getKeys(true)
            itemStacks.clear()
            keys.removeIf {
                data.getItemStack(it)?.let { item ->
                    itemStacks[it] = item
                    return@removeIf true
                }
                false
            }
            keys.forEach { data.set(it, null) }
        }
    }

    fun getItemStacks() = itemStacks

    fun getItem(id: String): ItemStack? = itemStacks[id]

    fun hasItem(id: String) = itemStacks.containsKey(id)

    fun addItem(id: String, itemStack: ItemStack) = itemStacks.put(id, itemStack)

    fun removeItem(id: String) = itemStacks.remove(id)

}