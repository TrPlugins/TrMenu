package cc.trixey.trmenu.common.api

import cc.trixey.trmenu.common.TrMenu

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