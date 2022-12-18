package cc.trixey.mc.invero.common

import cc.trixey.mc.invero.common.panel.PanelInstance

/**
 * @author Arasple
 * @since 2022/11/23 12:24
 */
interface PanelContainer : MutableCollection<Panel> {

    val panels: MutableCollection<Panel>

    override val size: Int
        get() = panels.size

    override fun add(element: Panel): Boolean {
        return panels.add(element.also {
            if (this is Parentable) {
                it.setParent(this)
            }
        })
    }

    override fun containsAll(elements: Collection<Panel>) = panels.containsAll(elements)

    override fun addAll(elements: Collection<Panel>): Boolean = elements.all { add(it) }

    override fun clear() = panels.clear()
    override fun remove(element: Panel) = panels.remove(element)

    override fun removeAll(elements: Collection<Panel>) = panels.removeAll(elements.toSet())

    override fun retainAll(elements: Collection<Panel>) = panels.retainAll(elements.toSet())

    override fun contains(element: Panel) = panels.contains(element)

    override fun isEmpty() = panels.isEmpty()

    override fun iterator() = panels.iterator()

    operator fun plusAssign(value: PanelInstance) {
        add(value)
    }

    operator fun minusAssign(value: Panel) {
        remove(value)
    }

    fun forEachPanel(block: Panel.() -> Unit) {
        panels.forEach(block)
    }

}