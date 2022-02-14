package trplugins.menu.api.action.base

import kotlin.reflect.KProperty

/**
 * TrMenu
 * trplugins.menu.api.action.base.ActionContent
 *
 * @author Score2
 * @since 2022/02/13 20:05
 */
class ActionContents : HashMap<String, Any> {

    constructor(defContent: Any? = null) : super() {
        defContent ?: return
        stringContent(defContent)
    }

    constructor(key: String, any: Any) {
        this[key] = any
    }

    fun stringContent(value: Any? = null): String {
        if (value != null) {
            this[defContentKey] = value
        }
        return this[defContentKey]?.toString()!!
    }

    override fun toString(): String {
        return runCatching { stringContent() }.getOrNull() ?: super.toString()
    }

    companion object {
        const val defContentKey = "content"
    }

}