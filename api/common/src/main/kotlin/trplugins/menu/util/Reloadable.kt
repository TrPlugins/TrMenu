package trplugins.menu.util

/**
 * @project TrMenu
 *
 * @author Score2
 * @since 2021/09/30 12:11
 */
internal object UNINITIALIZED_VALUE

class Reloadable<out T>(val initializer: () -> T) : Lazy<T> {

    private var _value: Any? = UNINITIALIZED_VALUE

    fun reload(): T {
        val result = initializer()
        _value = result
        return result
    }

    override fun isInitialized() =
        _value != UNINITIALIZED_VALUE

    override val value: T
        get() {
            if (_value != UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _value as T
            }
            return reload()
        }

    val get get() = value

    @Suppress("UNCHECKED_CAST")
    val getOrNull get() = _value as? T

}

fun <T> reloadable(initializer: () -> T) = Reloadable(initializer)