package trmenu.api.action.base

/**
 * TrMenu
 * trmenu.api.action.base.ActionDesc
 *
 * @author Score2
 * @since 2022/01/08 23:41
 */
interface ActionDesc {

    val name: Regex
    val parser: (Any, ActionOption) -> AbstractAction

    val registery get() = name to parser

}