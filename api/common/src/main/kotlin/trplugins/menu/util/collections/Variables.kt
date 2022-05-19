package trplugins.menu.util.collections

/**
 * @author Bkm016
 * @date 2021/1/31 17:21
 */
class Variables(source: String, regex: Regex, group: (List<String>) -> String = { it[1] }) {

    val element: List<Element> = source.toElements(regex, group)

    companion object {

        private fun String.toElements(regex: Regex, group: (List<String>) -> String): List<Element> {
            val list = mutableListOf<Element>()
            var index = 0
            regex.findAll(this).forEach {
                list.add(Element(substring(index, it.range.first)))
                list.add(Element(group.invoke(it.groupValues), true))
                index = it.range.last + 1
            }
            val last = Element(substring(index, length))
            if (last.value.isNotEmpty()) {
                list.add(last)
            }
            return list
        }

    }

    class Element(var value: String, var isVariable: Boolean = false)


}
