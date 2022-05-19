package trplugins.menu.module.internal.item

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.common.platform.function.submit
import taboolib.library.xseries.getItemStack
import taboolib.library.xseries.setItemStack
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * @author Arasple
 * @date 2020/7/26 8:53
 */
object ItemRepository {

    @Config("data/itemRepository.yml")
    private lateinit var data: Configuration
    private var writing = false
    val itemStacks = mutableMapOf<String, ItemStack>()

    @Schedule(delay = 20 * 60, period = 20 * 60, async = true)
    fun saveTask() = save(false)

    @Awake(LifeCycle.DISABLE)
    fun cancel() = save(true)

    private fun save(isCanceling: Boolean) {
        writing = true
        data.getKeys(true).filter { !itemStacks.keys.contains(it) }.forEach { data[it] = null }
        itemStacks.forEach { (id, item) -> data.setItemStack(id, item) }
        data.saveToFile()
        if (!isCanceling) submit(delay = 2L, async = !Bukkit.isPrimaryThread()) { writing = false }
    }

    @Schedule(delay = 20)
    fun load() {
        if (writing) return
        val keys = data.getKeys(true).toMutableList()
        itemStacks.clear()
        keys.removeIf {
            data.getItemStack(it)?.let { item ->
                itemStacks[it] = item
                return@removeIf true
            }
            false
        }
        keys.forEach { data[it] = null }
    }

    fun getItem(id: String): ItemStack? = itemStacks[id]

    fun hasItem(id: String) = itemStacks.containsKey(id)

    fun addItem(id: String, itemStack: ItemStack) = itemStacks.put(id, itemStack.clone())

    fun removeItem(id: String) = itemStacks.remove(id)

}