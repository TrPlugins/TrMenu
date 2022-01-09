package menu.module.display.item

import trmenu.api.action.base.ActionOption
import trmenu.module.display.MenuSession
import trmenu.module.internal.script.evalScript
import trmenu.util.collections.Variables

/**
 * @author Arasple
 * @date 2021/1/24 18:50
 */
class Lore(lore: List<String>) {

    val lore = lore
        .map {
            var condition: String? = null
            val line = buildString {
                Variables(it, ActionOption.Type.CONDITION.regex) { it[2] }.element.forEach { element ->
                    if (element.isVariable) condition = element.value
                    else append(element.value)
                }
            }

            line to condition
        }

    fun parse(session: MenuSession): List<String> {
        val lores = mutableListOf<String>()
        lore
            .forEach { (line, condition) ->
                if (condition == null || session.evalScript(condition).asBoolean()) {
                    lores.addAll(session.parse(line).split("\n"))
                }
            }

        return lores
    }

}