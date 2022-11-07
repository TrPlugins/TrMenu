package cc.trixey.mc.trmenu.invero.module

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

    operator fun get(index: Int) = pages[index]

    fun getPage() = pages[pageIndex]

    fun getPages() = pages

    fun addPage(layer: MappedElements) = pages.add(layer)

    abstract fun nextPage()

    abstract fun previousPage()

    abstract fun switchPage(page: Int)

}