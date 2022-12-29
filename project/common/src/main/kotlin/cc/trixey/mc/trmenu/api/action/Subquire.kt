package cc.trixey.mc.trmenu.api.action

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.Subquire
 *
 * @author Score2
 * @since 2022/12/29 22:29
 */
open class Subquire<T>(val condition: String?, val sub: Subquire<T>) {

    fun eval(): Boolean {
        if (condition == null) {
            return true
        }

        TODO("kether or javascript")
    }

    fun result(): T? =
        if (eval()) {
            sub.result()
        } else {
            null
        }

}