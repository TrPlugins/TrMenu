package cc.trixey.mc.invero.common

/**
 * @author Arasple
 * @since 2022/10/29 10:57
 *
 * 子、父级实现
 */
interface Parentable {

    fun getChildren(): List<Parentable>?

    fun getParent(): Parentable?

}