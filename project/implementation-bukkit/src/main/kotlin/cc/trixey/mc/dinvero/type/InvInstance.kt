package cc.trixey.mc.dinvero.type

import cc.trixey.mc.dinvero.Invero
import cc.trixey.mc.dinvero.InveroManager
import cc.trixey.mc.dinvero.InveroPool
import cc.trixey.mc.dinvero.event.InveroPostOpenEvent
import cc.trixey.mc.dinvero.nms.WindowProperty
import cc.trixey.mc.dinvero.nms.refreshWindowItems
import cc.trixey.mc.dinvero.nms.sendWindowOpen
import cc.trixey.mc.dinvero.nms.sendWindowSetSlot
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.warning

/**
 * @author Arasple
 * @since 2022/10/21
 */
open class InvInstance(
    override val pool: InveroPool,
    final override val property: WindowProperty,
    title: String,
) : Invero() {

    override var title: String = title
        set(value) {
            field = value
            if (!view.hasViewer()) {
                warning("Attempt to change title to [$value] while there is no viewers")
            }
            forViewers {
                it.sendWindowOpen(pool.index, property, value)
                it.refreshWindowItems(pool.index, contents)
            }
        }

    override fun open(player: Player) {
        val previous: Invero? = InveroManager.findViewingInvero(player)?.also {
            it.view.close(player, false)
        }

        InveroPostOpenEvent(player, this, previous).apply {
            call()
            postOpenCallback(this)

            if (!isCancelled) {
                view.setViewing(player)

                if (isUsingBukkitInventory()) {
                    player.openInventory(bukkitInveroHolder!!.inventory)
                } else {
                    player.sendWindowOpen(pool.index, property, title)
                }

                // TODO (BUKKIT ID?)
                refreshWindow()
                openedCallback(this@InvInstance)
            }
        }
    }

    override fun setPlayerContents(itemStacks: Array<ItemStack?>, update: Boolean) {
        property.slotsPlayerContents.forEachIndexed { i, slot ->
            contents[slot] = itemStacks[i]
        }
        if (update) refreshWindow()
    }

    override fun getItem(slot: Int): ItemStack? {
        return contents.getOrNull(slot)
    }

    override fun refreshItem(slot: Int) {
        forViewers {
            it.sendWindowSetSlot(pool.index, slot, contents[slot])
        }
    }

    override fun refreshItems(vararg slots: Int) {
        forViewers { viewer ->
            slots.forEach {
                viewer.sendWindowSetSlot(pool.index, it, contents[it])
            }
        }
    }

    override fun setItem(slot: Int, itemStack: ItemStack?, update: Boolean) {
        contents[slot] = itemStack
        if (update) forViewers {
            it.sendWindowSetSlot(pool.index, slot, itemStack)
        }
    }

    override fun removeItem(slot: Int, update: Boolean) {
        setItem(slot, null, update)
    }

    override fun removeItems(vararg slots: Int, update: Boolean) {
        slots.forEach {
            contents[it] = null
        }
        if (update) refreshWindow()
    }

    override fun setItems(items: HashMap<Int, ItemStack>, update: Boolean) {
        items.forEach { (k, v) ->
            contents[k] = v
        }
        if (update) refreshWindow()
    }

    override fun clear(update: Boolean) {
        contents.replaceAll { null }
        if (update) refreshWindow()
    }

    override fun refreshWindow() {
        forViewers {
            it.refreshWindowItems(pool.index, contents)
        }
    }

    override fun tick() {
    }

}