package cc.trixey.mc.trmenu.invero.module.base

import cc.trixey.mc.trmenu.invero.module.PanelInstance
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/6 16:51
 */
abstract class BasePagedPanel(
    scale: Pair<Int, Int>,
    pos: Int,
    weight: PanelWeight
) : PanelInstance(scale, pos, weight) {

    /**
     * Current page index
     */
    abstract var pageIndex: Int

    /**
     * Current max page index
     */
    abstract val maxPageIndex: Int

    /**
     * Switch to the next page
     */
    abstract fun nextPage(): Int

    /**
     * Switch to the previous page
     */
    abstract fun previousPage(): Int

    /**
     * Switch to a certain page
     */
    abstract fun switchPage(page: Int)

}