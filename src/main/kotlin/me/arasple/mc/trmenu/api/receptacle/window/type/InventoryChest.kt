package me.arasple.mc.trmenu.api.receptacle.window.type

import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import me.arasple.mc.trmenu.api.receptacle.window.ReceptacleType
import org.bukkit.event.inventory.InventoryType
import kotlin.properties.Delegates

/**
 * @author Arasple
 * @date 2020/11/29 10:59
 */
class InventoryChest(rows: Int = 3, title: String = "Chest") : Receptacle(InventoryType.CHEST, ReceptacleType.ofRows(rows), title) {

    var rows: Int by Delegates.observable(rows) { _, _, value ->
        type = ReceptacleType.ofRows(value)
    }

}