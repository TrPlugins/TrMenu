package trplugins.menu.module.internal.data

/**
 * @author Arasple
 * @date 2021/1/27 11:49
 */
@JvmInline
value class DataMap(val data: MutableMap<String, Any?> = mutableMapOf()) {

    operator fun set(key: String, value: String) {
        data[key] = value
    }

    operator fun get(key: String): Any? {
        return data[key]
    }

    fun remove(key: String) {
        data.remove(key)
    }

}