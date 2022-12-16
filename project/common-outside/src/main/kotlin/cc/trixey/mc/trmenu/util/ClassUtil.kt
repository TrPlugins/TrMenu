package cc.trixey.mc.trmenu.util

import taboolib.common.TabooLibCommon
import taboolib.common.io.runningClasses
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.ReflexClass
import java.lang.reflect.Modifier

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.ClassUtil
 *
 * @author Score2
 * @since 2022/11/26 0:17
 */

fun <T> List<Class<*>>.fromClassesCollect(`super`: Class<T>, deep: Boolean = false, insted: Boolean = false) =
    toTypedArray().fromClassesCollect(`super`, deep, insted)


fun <T> Array<Class<*>>.fromClassesCollect(`super`: Class<T>, deep: Boolean = false, insted: Boolean = false): MutableList<Class<out T>> =
    mutableListOf<Class<out T>>().also { list ->
        this.forEach { `class` ->
            `class`.fromClassCollect(`super`, deep, insted).forEach { list.add(it) }
        }
    }

/**
 * 从给定类中以指定父类筛选, 同时支持从 Companion 和 INSTANCE 中筛选
 * 获得后的 Class<T> 可使用 taboolib.io.getInstance() 获取指定对象
 *
 * @param super 父类
 * @param deep 范围增加至类中类
 * @param insted 从 Companion 和 INSTANCE 中筛选
 */
fun <T> Class<*>.fromClassCollect(`super`: Class<T>, deep: Boolean = false, insted: Boolean = false): MutableList<Class<out T>> =
    mutableListOf<Class<out T>>().also { list ->
        if (insted) {
            // 获取 Kotlin Companion 字段
            val field = if (simpleName == "Companion") {
                val companion = Class.forName(name.substringBeforeLast('$'), false, TabooLibCommon::class.java.classLoader)
                ReflexClass.of(companion).getField("Companion", findToParent = false, remap = false)
            }
            // 获取 Kotlin Object 字段
            else {
                ReflexClass.of(this).getField("INSTANCE", findToParent = false, remap = false)
            }
            runCatching {
                field.get()!!.javaClass.asSubclass(`super`)
            }.getOrNull().also {
                list.add(it ?: return@also)
            }
        }
        if (deep) {
            this.classes.fromClassesCollect(`super`, deep, insted).forEach { list.add(it) }
        }
        if (Modifier.isAbstract(this.modifiers)) return@also
        runCatching {
            asSubclass(`super`)
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