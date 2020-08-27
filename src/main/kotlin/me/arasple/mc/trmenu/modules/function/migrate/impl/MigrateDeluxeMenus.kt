package me.arasple.mc.trmenu.modules.function.migrate.impl

import com.extendedclip.deluxemenus.menu.Menu
import me.arasple.mc.trmenu.modules.function.migrate.Migrate

/**
 * @author Arasple
 * @date 2020/8/16 21:11
 */
class MigrateDeluxeMenus : Migrate() {

    override fun depend(): String {
        return "DeluxeMenus"
    }

    override fun migrate() {
        Menu.getAllMenus().forEach {
            it.inventoryType
            it.menuCommands
            it.name
            it.menuTitle
            it.size
            it.menuItems
            it.updateInterval
            it.openRequirements
            it.openHandler
            it.closeHandler

            it.argUsageMessage

            // UNSUPPORTED - it.registersCommand()
        }
    }

}