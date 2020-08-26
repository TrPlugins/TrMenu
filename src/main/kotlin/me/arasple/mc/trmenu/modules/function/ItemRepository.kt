package me.arasple.mc.trmenu.modules.function

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
import me.arasple.mc.trmenu.util.Tasks
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
    fun saveTask() = save(false)

    @TFunction.Cancel
    fun cancel() = save(true)

    fun save(isCanceling: Boolean) {
        Mirror.eval("ItemRepository:onSave(async)") {
            writing = true
            data.getKeys(true).filter { !itemStacks.keys.contains(it) }.forEach { data.set(it, null) }
            itemStacks.forEach { (id, item) -> data.set(id, item) }
            data.saveToFile()
            if (!isCanceling) {
                Tasks.delay(2) {
                    writing = false
                }
            }
        }
    }

    @TSchedule(delay = 20)
    private fun load() {
        if (writing) return
        Tasks.task(true) {
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

    fun addItem(id: String, itemStack: ItemStack) = itemStacks.put(id, itemStack.clone())

    fun removeItem(id: String) = itemStacks.remove(id)

}