package trplugins.menu.util.concurrent

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author Score2
 * @since 2021/09/28 15:32
 */
class TaskConcurrent<Task, Result>(val tasks: List<Task>, threads: Int) {

    private val poolExecutor =
        Executors.newFixedThreadPool(
            if (threads < 1) 1
            else if (threads > Runtime.getRuntime().availableProcessors())
                Runtime.getRuntime().availableProcessors()
            else threads
        )!!

    fun start(
        process: (Task) -> Result,
        succeed: (List<Pair<Task, Result>>) -> Unit = {},
        catching: ((Throwable) -> List<Pair<Task, Result>>)? = null
    ): CompletableFuture<List<Pair<Task, Result>>> {
        val future: CompletableFuture<List<Pair<Task, Result>>> = CompletableFuture.supplyAsync {
            val tasksFuture = mutableListOf<Future<Pair<Task, Result>>>()
            val tasksResult = mutableListOf<Pair<Task, Result>>()
            tasks.forEach {
                tasksFuture.add(poolExecutor.submit<Pair<Task, Result>> { Pair(it, process.invoke(it)) })
            }
            tasksFuture.forEach {
                try {
                    tasksResult.add(it.get())
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            tasksResult
        }
        future.thenAccept(succeed)
        catching?.let { future.exceptionally(catching) }
        return future
    }

}