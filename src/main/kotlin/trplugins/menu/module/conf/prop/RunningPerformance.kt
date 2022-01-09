package trplugins.menu.module.conf.prop

/**
 * @author Score2
 * @since 2021/09/28 16:29
 */
enum class RunningPerformance(val priority: Int) {
    HIGH(1), NORMAL(2), LOW(3);

    val pools: Int get() = Runtime.getRuntime().availableProcessors() / priority

}