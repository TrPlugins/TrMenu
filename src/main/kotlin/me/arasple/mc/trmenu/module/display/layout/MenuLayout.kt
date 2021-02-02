package me.arasple.mc.trmenu.module.display.layout

/**
 * @author Arasple
 * @date 2021/1/24 20:54
 */
inline class MenuLayout(private val layouts: Array<Layout>) {

    fun search(iconKey: String, pages: List<Int>): Map<Int, Set<Int>> {
        val ps = mutableMapOf<Int, MutableSet<Int>>()

        layouts.forEachIndexed { index, layout ->
            layout.keys[iconKey]?.let {
                ps.computeIfAbsent(index) { mutableSetOf() }
                    .addAll(it)
            }
        }

        pages.forEach { ps.computeIfAbsent(it) { mutableSetOf() } }

        return ps
    }

    operator fun get(page: Int): Layout {
        return layouts[page]
    }

    fun getSize(): Int {
        return layouts.size
    }

}