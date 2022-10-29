package cc.trixey.mc.trmenu.legacy.invero

import cc.trixey.mc.trmenu.legacy.invero.event.InveroCloseEvent
import cc.trixey.mc.trmenu.legacy.invero.event.InveroDragEvent
import cc.trixey.mc.trmenu.legacy.invero.event.InveroInteractEvent
import cc.trixey.mc.trmenu.legacy.invero.event.InveroPostOpenEvent
import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import cc.trixey.mc.trmenu.legacy.invero.window.InteractType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayDeque

/**
 * @author Arasple
 * @since 2022/10/21
 */
@Deprecated("")
abstract class Invero(viewer: UUID?) {

    internal val interactCallback = CopyOnWriteArrayList<(event: InveroInteractEvent) -> Unit>()

    internal var closeCallback: ((event: InveroCloseEvent) -> Unit) = {}

    internal var postOpenCallback: ((event: InveroPostOpenEvent) -> Unit) = {}

    internal var openedCallback: ((invero: Invero) -> Unit) = {}

    fun onClose(e: (event: InveroCloseEvent) -> Unit) {
        closeCallback = e
    }

    fun onPostOpen(e: (event: InveroPostOpenEvent) -> Unit) {
        postOpenCallback = e
    }

    fun onOpen(e: (invero: Invero) -> Unit) {
        openedCallback = e
    }

    fun onInteract(e: (event: InveroInteractEvent) -> Unit) {
        interactCallback += e
    }

    /**
     * 当前容器的观众
     */
    val view: InveroView by lazy {
        InveroView(this, viewer)
    }

    /**
     * 容器物品
     */
    val contents by lazy {
        CopyOnWriteArrayList(arrayOfNulls<ItemStack?>(property.entireWindowSize))
    }

    /**
     * 容器池
     *
     * @see InveroPool
     */
    abstract val pool: InveroPool

    /**
     * 容器类型
     * 包含详细的槽位指南
     *
     * @see WindowProperty
     */
    abstract val property: WindowProperty

    /**
     * 容器的标题
     */
    abstract var title: String

    /**
     * 为玩家打开容器
     */
    abstract fun open()

    /**
     * 容器内的物品管理
     *
     * Invero 的容器物品包括打开的容器页面和可用的玩家背包
     * 默认情况下打开，玩家的物品会被写入此内容，但对其的任何编辑不会影响玩家的真实物品，直至容器关闭
     */
    abstract fun getItem(slot: Int): ItemStack?

    abstract fun refreshItem(slot: Int)

    abstract fun refreshItems(vararg slots: Int)

    abstract fun setItem(slot: Int, itemStack: ItemStack?, update: Boolean = true)

    abstract fun removeItem(slot: Int, update: Boolean = true)

    abstract fun removeItems(vararg slots: Int, update: Boolean = true)

    abstract fun setItems(items: HashMap<Int, ItemStack>, update: Boolean = true)

    abstract fun setPlayerContents(itemStacks: Array<ItemStack?>, update: Boolean = true)

    abstract fun clear(update: Boolean = true)

    abstract fun tick()

    fun release() {
        InveroManager.destory(this)
    }

    fun forViewer(function: (Player) -> Unit) {
        view.forViewer(function)
    }

    abstract fun refreshWindow()

    fun amend() {
//        refreshWindow()
        contents.forEachIndexed { index, itemStack ->
            if (itemStack == null) {
                refreshItem(index)
            }
        }
    }

    val deque by lazy {
        ArrayDeque<InteractType>(property.entireWindowSize)
    }

    fun handleDragEvent(type: InteractType) {
        deque.add(type)
        if (deque.first().slot == -999 && deque.last().slot == -999) {
            deque.removeFirst()
            deque.removeLast()

            val slots = deque.map { it.slot }
            val dragType = if (type.button == 0) {
                InveroDragEvent.DragType.SINGLE
            } else {
                InveroDragEvent.DragType.EVEN
            }

            view.getViewer()?.let {
                InveroDragEvent(it, this, dragType, slots).call()
                it.sendMessage(
                    """
                §3——————————————————————————————§f
                DragType: ${dragType.name}
                Slots: $slots
                
                IType: ${type.name}
                    """.trimIndent()
                )
            }
            deque.clear()
        }
    }

}