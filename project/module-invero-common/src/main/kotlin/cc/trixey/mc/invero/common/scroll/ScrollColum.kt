package cc.trixey.mc.invero.common.scroll

import cc.trixey.mc.invero.common.base.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/18 13:00
 */
@JvmInline
value class ScrollColum(val elements: Array<ElementAbsolute?>) {

    operator fun set(index: Int, element: ElementAbsolute?) {
        elements[index] = element
    }

    operator fun get(index: Int): ElementAbsolute? {
        return elements[index]
    }

    fun safeCheck(columSize: Int): ScrollColum {
        return if (elements.size <= columSize) this else error("Expected colum size is $columSize")
    }

}