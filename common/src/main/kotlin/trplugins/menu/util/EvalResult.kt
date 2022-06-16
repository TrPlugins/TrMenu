package trplugins.menu.util

/**
 * @author Arasple
 * @date 2021/1/31 11:53
 */
@JvmInline
value class EvalResult(val any: Any? = null) {

    fun asBoolean(def: Boolean = false): Boolean {
        return when (any) {
            is Boolean -> any
            is String -> Regexs.parseBoolean(any)
            else -> def || Regexs.parseBoolean(any.toString())
        }
    }

    fun asString(): String {
        return any.toString()
    }

    companion object {

        val TRUE = EvalResult(true)

        val FALSE = EvalResult(false)

    }

}
