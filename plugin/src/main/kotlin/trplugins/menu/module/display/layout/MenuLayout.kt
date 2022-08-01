package trplugins.menu.module.display.layout

/**
 * @author Arasple
 * @date 2021/1/24 20:54
 */
@JvmInline
value class MenuLayout(internal val layouts: Array<Layout>) {

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

    companion object {

        val commonKeys = listOf(
            '#',
            '-',
            '@',
            '|',
            '=',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z'
        )


    }

}