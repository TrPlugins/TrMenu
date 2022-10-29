package cc.trixey.mc.trmenu.legacy.invero.type

import cc.trixey.mc.trmenu.legacy.invero.Invero
import cc.trixey.mc.trmenu.legacy.invero.InveroManager
import cc.trixey.mc.trmenu.legacy.invero.InveroPool
import cc.trixey.mc.trmenu.legacy.invero.event.InveroPostOpenEvent
import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import cc.trixey.mc.trmenu.legacy.invero.nms.refreshWindowItems
import cc.trixey.mc.trmenu.legacy.invero.nms.sendWindowOpen
import cc.trixey.mc.trmenu.legacy.invero.nms.sendWindowSetSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.warning
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/21
 */
open class InvInstance(
    override val pool: InveroPool,
    final override val property: WindowProperty,
    title: String,
    viewer: UUID?,
) : Invero(viewer) {

    override var title: String = title
        set(value) {
            field = value
            if (!view.hasViewer()) {
                warning("Attempt to change title to [$value] while there is no viewers")
            }
            forViewer {
                it.sendWindowOpen(pool.index, property, value)
                it.refreshWindowItems(pool.index, contents)
            }
        }

    override fun open() {
        val player = view.getViewer()!!
        val previous: Invero? = InveroManager.findViewingInvero(player)?.also {
            it.view.close()
        }

        InveroPostOpenEvent(player, this, previous).apply {
            call()
            postOpenCallback(this)

            if (!isCancelled) {
                view.setViewing(player)
                player.sendWindowOpen(pool.index, property, title)

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
        forViewer {
            it.sendWindowSetSlot(pool.index, slot, contents[slot])
        }
    }

    override fun refreshItems(vararg slots: Int) {
        forViewer { viewer ->
            slots.forEach {
                viewer.sendWindowSetSlot(pool.index, it, contents[it])
            }
        }
    }

    override fun setItem(slot: Int, itemStack: ItemStack?, update: Boolean) {
        contents[slot] = itemStack
        if (update) forViewer {
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
        forViewer {
            it.refreshWindowItems(pool.index, contents)
        }
    }

    override fun tick() {
    }

}