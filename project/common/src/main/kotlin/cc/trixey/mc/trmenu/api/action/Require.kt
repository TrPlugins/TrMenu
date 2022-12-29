package cc.trixey.mc.trmenu.api.action

import taboolib.library.configuration.ConfigurationSection

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Requestion
 *
 * @author Score2
 * @since 2022/12/28 23:06
 */
class Require<T>(condition: String?, sub: Subquire<T>): Subquire<T>(condition, sub) {

    fun saveToSection(): ConfigurationSection {
        TODO()
    }

}