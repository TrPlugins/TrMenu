package cc.trixey.mc.trmenu

import cc.trixey.mc.trmenu.api.TrMenuAPI
import cc.trixey.mc.trmenu.util.Conclusion

/**
 * ExampleProject
 * com.github.username.Common
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
object TrMenu {

    lateinit var api: TrMenuAPI
        private set

    fun register(api: TrMenuAPI) {
        TrMenu.api = api
    }

    fun <T> Collection<Conclusion<T>>.printStatistics(type: String = "bootstrapping"): Collection<Conclusion<T>> {
        if (!any { it.stats == Conclusion.Stats.FAIL }) {
            return this
        }
        println("§c[TrMenu] §8Unexpected exception while $type:")
        forEach {
            println("         §8${it.dumpProcedureFirst() ?: return@forEach}")
        }
        return this
    }
}