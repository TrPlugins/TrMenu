package cc.trixey.trmenu.core

import cc.trixey.trmenu.common.MenuOptions

/**
 * TrMenu
 * cc.trixey.trmenu.core.DefaultMenuOptions
 *
 * @author Arasple
 * @since 2023/1/13 14:07
 */
class DefaultMenuOptions(
    val defaultPage: Int,
    val hidePlayerStorage: Int,
    val minInteractInterval: Int,
    val dependPlaceholderExpansions: Set<String>
) : MenuOptions