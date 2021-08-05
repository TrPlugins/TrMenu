package me.arasple.mc.trmenu.module.internal.item

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.submit
import taboolib.library.xseries.getItemStack
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile

/**
 * @author Arasple
 * @date 2020/7/26 8:53
 */
object ItemRepository {

    @Config("data/itemRepository.yml")
    private lateinit var data: SecuredFile
    private var writing = false
    private val itemStacks = mutableMapOf<String, ItemStack>()

    init {
        submit(delay = (20 * 60), period = (20 * 60), async = true) {
            saveTask()
        }
        submit(delay = 20) {
            load()
        }
    }

    fun saveTask() = save(false)

//    @TFunction.Cancel - 暂时无法解决
    fun cancel() = save(true)

    private fun save(isCanceling: Boolean) {
        writing = true
        data.getKeys(true).filter { !itemStacks.keys.contains(it) }.forEach { data.set(it, null) }
        itemStacks.forEach { (id, item) -> data.set(id, item) }
        data
        if (!isCanceling) submit(delay = 2L, async = !Bukkit.isPrimaryThread()) { writing = false }
    }

    private fun load() {
        if (writing) return
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

    fun getItemStacks() = itemStacks

    fun getItem(id: String): ItemStack? = itemStacks[id]

    fun hasItem(id: String) = itemStacks.containsKey(id)

    fun addItem(id: String, itemStack: ItemStack) = itemStacks.put(id, itemStack.clone())

    fun removeItem(id: String) = itemStacks.remove(id)

}