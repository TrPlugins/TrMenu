package io.insinuate.utils.concurrent

import me.arasple.mc.trmenu.TrMenu
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author Score2
 * @since 2021/09/28 15:32
 */
class TaskConcurrent<Task, Result>(val tasks: List<Task>, threads: (Int) -> Int = { it }) {

    private val poolExecutor =
        Executors.newFixedThreadPool(if (TrMenu.SETTINGS.getBoolean("Options.Multi-Thread", true)) threads(TrMenu.performance.pools) else 1)!!

    fun start(
        process: (Task) -> Result,
        succeed: (List<Result>) -> Unit = {},
        catching: ((Throwable) -> List<Result>)? = null
    ): CompletableFuture<List<Result>> {
        val future: CompletableFuture<List<Result>> = CompletableFuture.supplyAsync {
            val tasksFuture = mutableListOf<Future<Result>>()
            val tasksResult = mutableListOf<Result>()
            tasks.forEach {
                tasksFuture.add(poolExecutor.submit<Result> { process.invoke(it) })
            }
            tasksFuture.forEach {
                kotlin.runCatching { tasksResult.add(it.get()) }
            }
            tasksResult
        }
        future.thenAccept(succeed)
        catching?.let { future.exceptionally(catching) }
        return future
    }

}