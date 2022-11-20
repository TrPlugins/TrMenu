package cc.trixey.mc.invero.common.scroll

import cc.trixey.mc.invero.common.base.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/18 13:00
 */
@JvmInline
value class ScrollColum(val elements: ArrayList<ElementAbsolute>) {

    fun safeCheck(columSize: Int): ScrollColum {
        return if (elements.size <= columSize) this else error("Expected colum size is $columSize")
    }

}