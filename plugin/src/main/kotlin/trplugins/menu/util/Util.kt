package trplugins.menu.util

import taboolib.module.configuration.Configuration

/**
 * @author Arasple
 * @date 2021/2/19 22:40
 */
fun Throwable.print(title: String) {
    println("§c[TrMenu] §8$title")
    println("         §8${localizedMessage}")
    stackTrace.forEach {
        println("         §8$it")
    }
}

val Boolean.trueOrNull get() = if (this) true else null

// 未来需要改进该功能
fun String.parseSimplePlaceholder(map: Map<Regex, String>): String {
    var raw = this
    map.forEach { raw = raw.replace(it.key, it.value) }
    return raw
}

// 未来需要改进该功能
fun String.parseIconId(iconId: String) = parseSimplePlaceholder(mapOf("(?i)@iconId@".toRegex() to iconId))

fun Configuration.ignoreCase(path: String) = getKeys(true).find { it.equals(path, ignoreCase = true) } ?: path


// 极其不稳定的方法, 已停用
/*

inline fun <reified T> fromClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        if (Modifier.isAbstract(`class`.modifiers)) return@forEach
        list.add(runCatching {
            `class`.asSubclass(`super`).getConstructor().newInstance()
        }.getOrNull() ?: return@forEach)
    }
}
*/
/*

fun <T> List<Class<*>>.fromClassesCollect(`super`: Class<T>, newInstance: Boolean = false, deep: Boolean = false) =
    toTypedArray().fromClassesCollect(`super`, newInstance, deep)

fun <T> Array<Class<*>>.fromClassesCollect(`super`: Class<T>, newInstance: Boolean = false, deep: Boolean = false): MutableList<T> =
    mutableListOf<T>().also { list ->
        this.forEach { `class` ->
            `class`.fromClassCollect(`super`, newInstance, deep).forEach { list.add(it) }
        }
    }

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.fromClassCollect(`super`: Class<T>, newInstance: Boolean = false, deep: Boolean = false): MutableList<T> =
    mutableListOf<T>().also { list ->
        if (Modifier.isAbstract(this.modifiers)) return@also
        runCatching {
            getInstance(newInstance)!!.get() as T
        }.getOrNull().also {
            list.add(it ?: return@also)
        }

        if (deep) {
            this.classes.fromClassesCollect(`super`, deep).forEach { list.add(it) }
        }
    }
*/


/*

@Suppress("UNCHECKED_CAST")
fun <T> fromCompanionClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        val instance = runCatching { `class`.getProperty<Any>("Companion", true) as T }.getOrNull() ?: return@forEach
        list.add(instance)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> fromObjectClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        val instance = runCatching { `class`.getProperty<Any>("INSTANCE", true) as T }.getOrNull() ?: return@forEach
        list.add(instance)
    }
}*/
