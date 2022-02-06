package trplugins.menu.module.conf.prop

import taboolib.common.util.replaceWithOrder
import trplugins.menu.module.display.icon.Icon
import trplugins.menu.module.display.layout.MenuLayout

/**
 * @author Arasple
 * @date 2021/1/25 10:12
 */
class SerialzeResult(
    val type: Type,
    var state: State = State.SUCCESS,
    val errors: MutableList<String> = mutableListOf(),
    var result: Any? = null
) {

    fun succeed(): Boolean {
        return state == State.SUCCESS && result != null
    }

    fun submitError(error: SerializeError, vararg args: Any) {
        state = State.FAILED
        errors.add(SerializeError.formatInfo(error).replaceWithOrder(*args))
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
        FAILED,
        IGNORE

    }

    enum class Type {

        MENU,
        MENU_SETTING,
        MENU_LAYOUT,
        ICON,

    }

}