package cc.trixey.mc.trmenu.util

import taboolib.common.io.getInstance
import taboolib.common.io.runningClasses
import taboolib.library.reflex.Reflex.Companion.getProperty
import java.lang.reflect.Modifier

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.ClassUtil
 *
 * @author Score2
 * @since 2022/11/26 0:17
 */

/**
 * 从给定类中寻找相同父类的对象, 同时可以从 Companion 和 INSTANCE 中寻找
 */
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
        if (deep) {
            this.classes.fromClassesCollect(`super`, deep).forEach { list.add(it) }
        }
        if (Modifier.isAbstract(this.modifiers)) return@also
        runCatching {
            val `class` = asSubclass(`super`)
            `class`.getInstance(newInstance)!!.get() as T
        }.getOrNull().also {
            list.add(it ?: return@also)
        }
    }


/**
 * 从现有所有类中寻找相同父类的对象
 */
@Deprecated("")
inline fun <reified T> fromClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        if (Modifier.isAbstract(`class`.modifiers)) return@forEach
        list.add(runCatching {
            `class`.asSubclass(`super`).getConstructor().newInstance()
        }.getOrNull() ?: return@forEach)
    }
}

@Deprecated("")
@Suppress("UNCHECKED_CAST")
fun <T> fromCompanionClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        val instance = runCatching { `class`.getProperty<Any>("Companion", true) as T }.getOrNull() ?: return@forEach
        list.add(instance)
    }
}

@Deprecated("")
@Suppress("UNCHECKED_CAST")
fun <T> fromObjectClassesCollect(`super`: Class<T>) = mutableListOf<T>().also { list ->
    runningClasses.forEach { `class` ->
        val instance = runCatching { `class`.getProperty<Any>("INSTANCE", true) as T }.getOrNull() ?: return@forEach
        list.add(instance)
    }
}