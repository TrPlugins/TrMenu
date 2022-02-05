package trplugins.menu.api

import net.md_5.bungee.api.ChatColor.COLOR_CHAR
import taboolib.module.configuration.Type

/**
 * TrMenu
 * me.arasple.mc.trmenu.module.display.MenuType
 *
 * @author Score2
 * @since 2021/12/04 11:08
 */
val Type.suffixes get() = when (this) {
    Type.YAML -> arrayOf("yml", "yaml")
    Type.TOML -> arrayOf("tml", "toml")
    Type.JSON -> arrayOf("json")
    Type.HOCON -> arrayOf("conf")
    else -> arrayOf()
}

val Type.color get() = when (this) {
    Type.YAML -> COLOR_CHAR + "b"
    Type.TOML -> COLOR_CHAR + "2"
    Type.JSON -> COLOR_CHAR + "6"
    Type.HOCON -> COLOR_CHAR + "3"
    else -> COLOR_CHAR + "7"
}