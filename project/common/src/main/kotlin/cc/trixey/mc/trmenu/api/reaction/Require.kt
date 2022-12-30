package cc.trixey.mc.trmenu.api.reaction

import taboolib.library.configuration.ConfigurationSection

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Requestion
 *
 * @author Score2
 * @since 2022/12/28 23:06
 */
class Require<T>(val condition: String?, val value: T) {

    fun eval(): Boolean {
        if (condition == null) {
            return true
        }

        TODO("kether or javascript")
    }

    fun result(): T? =
        if (eval()) {
            value
        } else {
            null
        }

    fun saveToSection(): ConfigurationSection {
        TODO()
    }

}