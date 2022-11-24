package cc.trixey.mc.invero.common.panel.scroll

import cc.trixey.mc.invero.common.Element

/**
 * @author Arasple
 * @since 2022/11/18 13:00
 */
@JvmInline
value class ScrollColum(val elements: Array<Element?>) {

    operator fun set(index: Int, element: Element?) {
        elements[index] = element
    }

    operator fun get(index: Int): Element? {
        return elements[index]
    }

    fun safeCheck(columSize: Int): ScrollColum {
        return if (elements.size <= columSize) this else error("Expected colum size is $columSize")
    }

}