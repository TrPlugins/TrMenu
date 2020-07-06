package me.arasple.mc.trmenu.modules.expression

import io.izzel.taboolib.util.Strings

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 */
class Expression(val regex: Regex, var replace: String, var splitBy: String) {

    constructor(name: String, replace: String, splitBy: String) : this(Regex("(?i)$name"), replace, splitBy)

    fun parse(string: String): String = Strings.replaceWithOrder(replace, getArguments(string))

    private fun getArguments(string: String): Array<String> = string.split(splitBy).toMutableList().let {
        it.removeAt(0)
        return@let it.toTypedArray()
    }

}