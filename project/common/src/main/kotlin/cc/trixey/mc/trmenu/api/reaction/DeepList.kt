package cc.trixey.mc.trmenu.api.reaction

/**
 * TrMenu
 * cc.trixey.mc.trmenu.api.action.DeepList
 *
 * @author Score2
 * @since 2022/12/29 22:36
 */
class DeepList<T : Any>(
    parent: Collection<T>
) : ArrayList<Require<Any>>(
    parent.map { Require(null, it) }
) {

    fun list() {

    }

}