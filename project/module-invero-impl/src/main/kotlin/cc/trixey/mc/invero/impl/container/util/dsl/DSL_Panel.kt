package cc.trixey.mc.invero.impl.container.util.dsl

import cc.trixey.mc.invero.Element
import cc.trixey.mc.invero.panel.BaseScrollPanel
import cc.trixey.mc.invero.panel.scroll.ScrollDirection
import cc.trixey.mc.invero.panel.scroll.ScrollType
import cc.trixey.mc.invero.impl.container.panel.ScrollStandardPanel

/**
 * @author Arasple
 * @since 2022/11/23 10:38
 */


/**
 * ScrollStandardPanel
 */

fun ScrollStandardPanel.fillColum(block: (index: Int) -> Element) {
    addColum { it ->
        it.forEach { this[it] = block(it) }
    }
}

/**
 * BaseScrollPanel
 */

fun BaseScrollPanel.scrollType(value: ScrollType) {
    type = value
}

fun BaseScrollPanel.stopDead() {
    scrollType(ScrollType.stopAt(columViewSize))
}

fun BaseScrollPanel.scrollVertically() {
    direction = ScrollDirection(true)
}

fun BaseScrollPanel.scrollHorizontally() {
    direction = ScrollDirection(false)
}

fun BaseScrollPanel.scrollRandomDirection() {
    direction = ScrollDirection(arrayOf(true, false).random())
}

fun BaseScrollPanel.stopBlank(value: Int) {
    scrollType(ScrollType.stopAt(value))
}

fun BaseScrollPanel.loop() {
    scrollType(ScrollType.LOOP)
}

fun BaseScrollPanel.scrollRandomType() {
    scrollType(ScrollType.random())
}