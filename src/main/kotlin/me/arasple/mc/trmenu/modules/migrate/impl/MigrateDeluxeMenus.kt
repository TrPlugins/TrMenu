package me.arasple.mc.trmenu.modules.migrate.impl

import com.extendedclip.deluxemenus.DeluxeMenus
import me.arasple.mc.trmenu.modules.migrate.Migrate

/**
 * @author Arasple
 * @date 2020/8/16 21:11
 */
class MigrateDeluxeMenus : Migrate() {

    override fun depend(): String {
        return "DeluxeMenus"
    }

    override fun migrate() {
        DeluxeMenus.getInstance()
    }

}