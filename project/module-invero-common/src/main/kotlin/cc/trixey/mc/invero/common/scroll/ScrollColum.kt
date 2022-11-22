package cc.trixey.mc.invero.common.scroll

import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.base.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/18 13:00
 */
@JvmInline
value class ScrollColum(val elements: Array<Element?>) {
    constructor(it: List<ElementAbsolute>) : this(it.toTypedArray())

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