package cc.trixey.mc.trmenu.util

/**
 * TrMenu
 * cc.trixey.mc.trmenu.util.Conclusion
 *
 * @author Score2
 * @since 2022/12/10 0:51
 */
class Conclusion<T>(private var obj: T? = null) {

    var stats = Stats.INIT
        private set

    private val procedures = mutableListOf<String>()

    fun prove(failed: String? = null, block: () -> T?): Conclusion<T> {
        obj = runCatching(block).apply { stats = Stats.SUCC }.getOrElse {
            stats = Stats.FAIL
            if (failed != null) {
                addProcedure(failed)
            } else {
                addProcedure(it.message ?: return@getOrElse null)
            }

            null
        }
        return this
    }

    fun hasProcedure(): Boolean {
        return procedures.isNotEmpty()
    }

    fun addProcedure(content: String) {
        procedures.add(content)
    }

    fun dumpProcedures(): List<String> {
        return procedures.toList()
    }

    fun dumpProcedureFirst(): String? {
        return procedures.getOrNull(0)
    }

    val isAbsoluteSuccessful get() =
        stats == Stats.SUCC || stats == Stats.INIT && obj != null

    fun get(): T {
        return obj!!
    }

    fun getOrNull(): T? {
        return obj
    }

    enum class Stats {
        INIT,
        SUCC,
        FAIL
    }
}