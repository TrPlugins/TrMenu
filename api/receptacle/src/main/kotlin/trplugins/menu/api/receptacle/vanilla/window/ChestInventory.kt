package trplugins.menu.api.receptacle.vanilla.window

import kotlin.properties.Delegates

/**
 * @author Arasple
 * @date 2020/11/29 10:59
 */
class ChestInventory(rows: Int = 3, title: String = "Chest") : WindowReceptacle(WindowLayout.ofRows(rows), title) {

    var rows: Int by Delegates.observable(rows) { _, _, value ->
        type = WindowLayout.ofRows(value)
    }
}