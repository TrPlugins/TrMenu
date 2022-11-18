package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.module.element.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/18 13:00
 */
@JvmInline
value class ColumElements(val elements: ArrayList<ElementAbsolute>) {

    fun safeCheck(columSize: Int): ColumElements {
        return if (elements.size <= columSize) this else error("Expected colum size is $columSize")
    }

}