package cc.trixey.mc.trmenu.api.reaction

import cc.trixey.mc.trmenu.TrMenu
import cc.trixey.mc.trmenu.TrMenu.conditionalKether
import taboolib.common.platform.ProxyPlayer
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Requestion
 *
 * @author Score2
 * @since 2022/12/28 23:06
 */
class Require<T>(val key: String, val condition: String?, val value: T) {

    fun eval(player: ProxyPlayer): Boolean {
        if (condition == null) {
            return true
        }

        // TODO("javascript")

        return player.conditionalKether(condition)
    }

    fun result(player: ProxyPlayer): T? =
        if (eval(player)) {
            value
        } else {
            null
        }

    fun saveToSection(): ConfigurationSection {
        val section = Configuration.empty()
        section["condition"] = condition
        section[key] = value
        return section
    }

}