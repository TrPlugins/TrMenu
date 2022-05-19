package trplugins.menu.api.event

import trplugins.menu.module.internal.database.Database
import taboolib.platform.type.BukkitProxyEvent

/**
 * @Author sky
 * @Since 2020-08-14 14:52
 */
class CustomDatabaseEvent(val name: String, var database: Database? = null) : BukkitProxyEvent() {

    override val allowCancelled: Boolean
        get() = false
}