package cc.trixey.mc.invero.panel.generator

import cc.trixey.mc.invero.item.ElementAbsolute

/**
 * @author Arasple
 * @since 2022/11/22 13:14
 */
interface GeneratorPanel {

    var lastGenerated: List<ElementAbsolute>

    fun <T : ElementAbsolute, R> generator(
        data: List<R>,
        spawner: (R) -> T
    ) = generator(data, spawner, null, null)

    fun <T : ElementAbsolute, R> generator(
        data: List<R>,
        spawner: (R) -> T,
        filter: ((T) -> Boolean)? = null,
        sorter: ((T) -> Int)? = null,
    ) {
        val generator = Generator(data, spawner, filter, sorter)
        lastGenerated = generator.spawn()
    }

}