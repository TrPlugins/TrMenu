/**
 * modified version of Skedule
 *
 * from https://github.com/okkero/Skedule/
 */

@file:RuntimeDependencies(
    RuntimeDependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
)

package cc.trixey.trmenu.common.coroutine

import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.platform.BukkitPlugin
import kotlin.coroutines.*

private val plugin by lazy {
    BukkitPlugin.getInstance()
}

private val scheduler by lazy {
    Bukkit.getScheduler()
}

@OptIn(InternalCoroutinesApi::class)
class BukkitDispatcher(val plugin: JavaPlugin, val async: Boolean = false) : CoroutineDispatcher(), Delay {

    private val runTaskLater: (Plugin, Runnable, Long) -> BukkitTask = if (async) scheduler::runTaskLaterAsynchronously
    else scheduler::runTaskLater

    private val runTask: (Plugin, Runnable) -> BukkitTask = if (async) scheduler::runTaskAsynchronously
    else scheduler::runTask

    @ExperimentalCoroutinesApi
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = runTaskLater(plugin, { continuation.apply { resumeUndispatched(Unit) } }, timeMillis / 50)
        continuation.invokeOnCancellation { task.cancel() }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) return
        if (!async && Bukkit.isPrimaryThread()) block.run()
        else runTask(plugin, block)
    }

}

fun JavaPlugin.dispatcher(async: Boolean = false) = BukkitDispatcher(this, async)

fun Plugin.schedule(
    async: Boolean = false, co: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask {
    return schedule(async, co)
}

/**
 * Schedule a coroutine with the Bukkit Scheduler.
 *
 * @receiver The BukkitScheduler instance to use for scheduling tasks.
 */
fun launchAsync(co: suspend BukkitSchedulerController.() -> Unit): CoroutineTask {
    return launch(true, co)
}

fun launch(async: Boolean = false, co: suspend BukkitSchedulerController.() -> Unit): CoroutineTask {
    val controller = BukkitSchedulerController(scheduler)
    val block: suspend BukkitSchedulerController.() -> Unit = {
        try {
            start(async)
            co()

        } finally {
            cleanup()
        }
    }

    block.createCoroutine(receiver = controller, completion = controller).resume(Unit)

    return CoroutineTask(controller)
}

/**
 * Controller for Bukkit Scheduler coroutine
 *
 * @property plugin the Plugin instance to schedule the tasks bound to this coroutine
 * @property scheduler the BukkitScheduler instance to schedule the tasks bound to this coroutine
 * @property currentTask the task that is currently executing within the context of this coroutine
 * @property isRepeating whether this coroutine is currently backed by a repeating task
 */
@RestrictsSuspension
class BukkitSchedulerController(val scheduler: BukkitScheduler) : Continuation<Unit> {

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    private var schedulerDelegate: TaskScheduler = NonRepeatingTaskScheduler(plugin, scheduler)

    val currentTask: BukkitTask?
        get() = schedulerDelegate.currentTask

    val isRepeating: Boolean
        get() = schedulerDelegate is RepeatingTaskScheduler

    internal suspend fun start(async: Boolean = false) = suspendCoroutine { cont ->
        schedulerDelegate.doContextSwitch(async) { cont.resume(Unit) }
    }

    internal fun cleanup() {
        currentTask?.cancel()
    }

    override fun resumeWith(result: Result<Unit>) {
        cleanup()
        result.getOrThrow()
    }

    /**
     * Wait for __at least__ the specified amount of ticks. If the coroutine is currently backed by a non-repeating
     * task, a new Bukkit task will be scheduled to run the specified amount of ticks later. If this coroutine is
     * currently backed by a repeating task, the amount of ticks waited depends on the repetition resolution of the
     * coroutine. For example, if the repetition resolution is `10` and the `ticks` argument is `12`, it will result in
     * a delay of `20` ticks.
     *
     * @param ticks the amount of ticks to __at least__ wait for
     *
     * @return the actual amount of ticks waited
     */
    suspend fun wait(ticks: Long): Long = suspendCoroutine { cont ->
        schedulerDelegate.doWait(ticks, cont::resume)
    }

    /**
     * Relinquish control for as short an amount of time as possible. That is, wait for as few ticks as possible.
     * If this coroutine is currently backed by a non-repeating task, this will result in a task running at the next
     * possible occasion. If this coroutine is currently backed by a repeating task, this will result in a delay for as
     * short an amount of ticks as the repetition resolution allows.
     *
     * @return the actual amount of ticks waited
     */
    suspend fun yield(): Long = suspendCoroutine { cont ->
        schedulerDelegate.doYield(cont::resume)
    }

    /**
     * Switch to the specified SynchronizationContext. If this coroutine is already in the given context, this method
     * does nothing and returns immediately. Otherwise, the behaviour is documented in [newContext].
     *
     * @param context the context to switch to
     * @return `true` if a context switch was made, `false` otherwise
     */
    private suspend fun switchContext(async: Boolean = false): Boolean = suspendCoroutine { cont ->
        schedulerDelegate.doContextSwitch(async, cont::resume)
    }

    suspend fun synchronize() = switchContext(false)

    suspend fun asynchronize() = switchContext(true)

    /**
     * Force a new task to be scheduled in the specified context. This method will result in a new repeating or
     * non-repeating task to be scheduled. Repetition state and resolution is determined by the currently running currentTask.
     *
     * @param context the synchronization context of the new task
     */
    suspend fun newContext(async: Boolean = false): Unit = suspendCoroutine { cont ->
        schedulerDelegate.forceNewContext(async) { cont.resume(Unit) }
    }

    /**
     * Turn this coroutine into a repeating coroutine. This method will result in a new repeating task being scheduled.
     * The new task's interval will be the same as the specified resolution. Subsequent calls to [wait] and [yield]
     * will from here on out defer further execution to the next iteration of the repeating task. This is useful for
     * things like countdowns and delays at fixed intervals, since [wait] will not result in a new task being
     * spawned.
     */
    suspend fun repeating(resolution: Long): Long = suspendCoroutine { cont ->
        schedulerDelegate = RepeatingTaskScheduler(resolution, plugin, scheduler)
        schedulerDelegate.forceNewContext(getSynchronization()) { cont.resume(0) }
    }

}

class CoroutineTask internal constructor(private val controller: BukkitSchedulerController) {

    val currentTask
        get() = controller.currentTask

    val isSync
        get() = controller.currentTask?.isSync ?: false

    val isAsync
        get() = !(controller.currentTask?.isSync ?: true)

    fun cancel() = controller.resume(Unit)

}

private class RepetitionContinuation(val resume: (Long) -> Unit, val delay: Long = 0) {

    var passedTicks = 0L
    private var resumed = false

    fun tryResume(passedTicks: Long) {
        if (resumed) throw IllegalStateException("Already resumed")
        this.passedTicks += passedTicks
        if (this.passedTicks >= delay) {
            resumed = true
            resume(this.passedTicks)
        }
    }

}

private interface TaskScheduler {

    val currentTask: BukkitTask?

    fun doWait(ticks: Long, task: (Long) -> Unit)

    fun doYield(task: (Long) -> Unit)

    fun doContextSwitch(async: Boolean = false, task: (Boolean) -> Unit)

    fun forceNewContext(async: Boolean = false, task: () -> Unit)

}

private class NonRepeatingTaskScheduler(val plugin: Plugin, val scheduler: BukkitScheduler) : TaskScheduler {

    override var currentTask: BukkitTask? = null

    override fun doWait(ticks: Long, task: (Long) -> Unit) = runTaskLater(ticks) { task(ticks) }

    override fun doYield(task: (Long) -> Unit) = doWait(0, task)

    override fun doContextSwitch(async: Boolean, task: (Boolean) -> Unit) {
        if (async == getSynchronization()) task(false)
        else forceNewContext(async) { task(true) }
    }

    override fun forceNewContext(async: Boolean, task: () -> Unit) {
        runTask(async) { task() }
    }

    private fun runTask(async: Boolean = false, task: () -> Unit) {
        currentTask = if (!async) scheduler.runTask(plugin, task)
        else scheduler.runTaskAsynchronously(plugin, task)
    }

    private fun runTaskLater(ticks: Long, async: Boolean = false, task: () -> Unit) {
        currentTask = if (!async) scheduler.runTaskLater(plugin, task, ticks)
        else scheduler.runTaskLaterAsynchronously(plugin, task, ticks)
    }

}

private class RepeatingTaskScheduler(
    val interval: Long, val plugin: Plugin, val scheduler: BukkitScheduler
) : TaskScheduler {

    override var currentTask: BukkitTask? = null
    private var nextContinuation: RepetitionContinuation? = null

    override fun doWait(ticks: Long, task: (Long) -> Unit) {
        nextContinuation = RepetitionContinuation(task, ticks)
    }

    override fun doYield(task: (Long) -> Unit) {
        nextContinuation = RepetitionContinuation(task)
    }

    // TODO Be lazy if not yet started...maybe?
    override fun doContextSwitch(async: Boolean, task: (Boolean) -> Unit) {
        if (async == getSynchronization()) task(false)
        else forceNewContext(async) { task(true) }
    }

    override fun forceNewContext(async: Boolean, task: () -> Unit) {
        doYield { task() }
        runTaskTimer(async)
    }

    private fun runTaskTimer(async: Boolean = false) {
        currentTask?.cancel()
        val task: () -> Unit = { nextContinuation?.tryResume(interval) }
        currentTask = if (!async) scheduler.runTaskTimer(plugin, task, 0L, interval)
        else scheduler.runTaskTimerAsynchronously(plugin, task, 0L, interval)
    }

}

private fun getSynchronization() = Bukkit.isPrimaryThread()
