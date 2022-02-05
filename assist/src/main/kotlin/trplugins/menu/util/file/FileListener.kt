package trplugins.menu.util.file

import taboolib.common5.FileWatcher
import java.io.File


/**
 * @author Arasple
 * @date 2020/7/28 11:43
 */
object FileListener {

    private val listening = mutableSetOf<File>()

    fun isListening(file: File): Boolean {
        return watcher.hasListener(file)
    }

    fun listener(file: File, runnable: () -> Unit) {
        watcher.addSimpleListener(file, runnable)
        listening.add(file)
    }

    fun clear() {
        var count = 0
        listening.removeIf {
            val remove = !it.exists()
            if (remove) {
                watcher.removeListener(it)
                count++
            }
            remove
        }
        if (count > 0) {
            println("DEBUG: CLEARED $count unused listeners")
        }
    }

//    @TFunction.Cancel
    fun uninstall() {
        watcher.unregisterAll()
    }

    val watcher = FileWatcher()

}