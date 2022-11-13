package cc.trixey.mc.trmenu.invero.impl.panel

import cc.trixey.mc.trmenu.invero.module.Generator
import cc.trixey.mc.trmenu.invero.module.Window
import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute
import cc.trixey.mc.trmenu.invero.module.`object`.MappedElements
import cc.trixey.mc.trmenu.invero.module.`object`.PanelWeight

/**
 * @author Arasple
 * @since 2022/11/13 15:33
 */
class PagedGeneratorPanel(
    scale: Pair<Int, Int>, pos: Int, weight: PanelWeight
) : PagedStandardPanel(scale, pos, weight) {

    override var maxPageIndex: Int = 0

    lateinit var generator: Generator<ElementAbsolute>

    private var lastGenerated: List<ElementAbsolute> = listOf()
        set(value) {
            maxPageIndex = value.size / slots.size
            if (pagedElements.lastIndex > maxPageIndex) {
                pagedElements
            }
            field = value
        }

    override fun getPage(index: Int): MappedElements {
        if (pagedElements.isEmpty()) addPage(MappedElements())
        return pagedElements[0]
    }

    override fun renderPanel(window: Window) {
        genearte()
        super.renderPanel(window)
    }

    fun genearte(): Boolean {
        val generated = generator.generate()
        val fromIndex = pageIndex * slots.size
        val toIndex = (fromIndex + slots.size).coerceAtMost(generated.lastIndex)

        return if (generated.size <= fromIndex) {
            // not available
            false
        } else {
            lastGenerated = generated
            val fetch = generated.subList(
                fromIndex,
                toIndex
            )

            getPage().apply {
                slots.forEachIndexed { index, slot ->
                    if (index <= fetch.lastIndex) fetch[index].set(slot)
                    else removeElement(index)
                }
            }
            true
        }
    }

}