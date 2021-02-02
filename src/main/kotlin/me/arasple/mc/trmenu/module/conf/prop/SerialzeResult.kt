package me.arasple.mc.trmenu.module.conf.prop

import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.module.display.icon.Icon
import me.arasple.mc.trmenu.module.display.layout.MenuLayout

/**
 * @author Arasple
 * @date 2021/1/25 10:12
 */
class SerialzeResult(
    val type: Type,
    private var state: State = State.SUCCESS,
    val errors: MutableList<String> = mutableListOf(),
    var result: Any? = null
) {

    fun succeed(): Boolean {
        return state == State.SUCCESS && result != null
    }

    fun submitError(error: SerialzeError, vararg args: Any) {
        state = State.FAILED
        errors.add(Strings.replaceWithOrder(SerialzeError.formatInfo(error), *args))
    }

    @Suppress("UNCHECKED_CAST")
    fun asIcons(): Set<Icon> {
        return (result as List<Icon>).toSet()
    }

    fun asLayout(): MenuLayout {
        return result as MenuLayout
    }

    enum class State {

        SUCCESS,
        FAILED

    }

    enum class Type {

        MENU,
        MENU_SETTING,
        MENU_LAYOUT,
        ICON,

    }

}