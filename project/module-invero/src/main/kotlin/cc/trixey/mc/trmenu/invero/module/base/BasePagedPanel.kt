package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight
import java.util.*

/**
 * @author Arasple
 * @since 2022/11/6 16:51
 */
abstract class BasePagedPanel(
    scale: Pair<Int, Int>, pos: Int, weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * Current page index
     */
    abstract var pageIndex: Int

    /**
     * Current pages amount
     */
    val maxPageIndex: Int
        get() {
            return pages.size - 1
        }

    private val pages = LinkedList<MappedElements>()

    override val slotsUnoccupied: List<Int>
        get() = slotsUnoccupied()

    override val slotsOccupied: Set<Int>
        get() = slotsOccupied()

    fun slotsUnoccupied(index: Int = pageIndex) = slots - slotsOccupied(index)

    fun slotsOccupied(index: Int = pageIndex) = getPage(index).slotsOccupied

    operator fun get(index: Int) = pages[index]

    fun getPage(index: Int = pageIndex) = pages[index]

    fun getPages() = pages

    fun addPage(layer: MappedElements): Int {
        pages.add(layer)
        return pages.size - 1
    }

    abstract fun nextPage(): Int

    abstract fun previousPage(): Int

    abstract fun switchPage(page: Int)

}