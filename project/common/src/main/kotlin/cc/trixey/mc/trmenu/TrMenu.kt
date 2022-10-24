package cc.trixey.mc.trmenu

import cc.trixey.mc.trmenu.api.TrMenuAPI

/**
 * ExampleProject
 * com.github.username.Common
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
object TrMenu {

    lateinit var api: TrMenuAPI
        private set

    fun register(api: TrMenuAPI) {
        TrMenu.api = api
    }
}