package cc.trixey.mc.trmenu.api

import cc.trixey.mc.trmenu.TrMenu

/**
 * TrMenu
 * io.insinuate.trplugins.menu.api.TrMenuAPI
 *
 * @author Score2
 * @since 2022/09/11 11:08
 */
interface TrMenuAPI {

    companion object {

        val api get() = TrMenu.api

    }

}