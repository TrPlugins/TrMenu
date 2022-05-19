package trplugins.menu.module.internal.migrate.plugin

import com.extendedclip.deluxemenus.menu.Menu
import org.bukkit.event.inventory.InventoryType
import taboolib.module.configuration.Configuration

/**
 * @author Arasp
 * @date 2021/2/20 19:07
 */
object MigrateDeluxeMenus : MigratePlugin("DeluxeMenus") {

    override fun migrate() {
        Menu.getAllMenus().forEach {
            val name = it.menuName
            val defUpdate = it.updateInterval
            Configuration.empty().run {

                if (it.inventoryType != InventoryType.CHEST) {
                    this["Type"] = it.name
                }
                if (it.menuCommands.isNotEmpty()) {
                    this["Bindings.Commands"] = it.menuCommands
                }
                it.openHandler
                this["Title"] = it.menuTitle
                this["Size"] = it.size
                this["Icons"] = it.menuItems

                it.menuItems
                it.openHandler
                it.closeHandler
                it.registersCommand()
            }
        }
    }

}