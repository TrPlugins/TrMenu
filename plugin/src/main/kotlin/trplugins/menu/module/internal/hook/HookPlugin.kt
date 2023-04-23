package trplugins.menu.module.internal.hook

import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.SkipTo
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import taboolib.module.lang.sendLang
import trplugins.menu.module.internal.hook.impl.*
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * @author Arasple
 * @date 2021/1/26 22:04
 */
@SkipTo(LifeCycle.ENABLE)
object HookPlugin {

    fun printInfo() {
        registry.filter { it.isHooked }.forEach {
            console().sendLang("Plugin-Dependency-Hooked", it.name)
        }
    }

    private val registry by lazy {
        mutableListOf<HookAbstract>().also {
            runningClasses.forEach { `class` ->
                if (Modifier.isAbstract(`class`.modifiers)) return@forEach
                if (`class`.superclass != HookAbstract::class.java) return@forEach

                it.add(`class`.asSubclass(HookAbstract::class.java).getConstructor().newInstance())
            }
        }.toTypedArray()
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(clazz: Class<T>) = registry.find { it.javaClass == clazz } as T

    operator fun <T : Any> get(clazz: KClass<T>) = this[clazz.java]

    fun getHeadDatabase(): HookHeadDatabase {
        return get(HookHeadDatabase::class.java)
    }

    fun getPlayerPoints(): HookPlayerPoints {
        return get(HookPlayerPoints::class.java)
    }

    fun getSkinsRestorer(): HookSkinsRestorer {
        return get(HookSkinsRestorer::class.java)
    }

    fun getItemsAdder(): HookItemsAdder {
        return get(HookItemsAdder::class.java)
    }

    fun getFloodgate(): HookFloodgate {
        return get(HookFloodgate::class.java)
    }

    fun getVault(): HookVault {
        return get(HookVault::class.java)
    }

    fun getFastScript(): HookFastScript {
        return get(HookFastScript::class.java)
    }

    fun getZaphkiel(): HookZaphkiel {
        return get(HookZaphkiel::class.java)
    }

    fun getTriton(): HookTriton {
        return get(HookTriton::class.java)
    }

}