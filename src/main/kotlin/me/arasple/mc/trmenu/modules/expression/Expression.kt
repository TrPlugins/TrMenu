package me.arasple.mc.trmenu.modules.expression

import io.izzel.taboolib.util.Strings

/**
 * @author Arasple
 * @date 2020/3/1 22:45
 */
enum class Expression(val regex: Regex, var replace: String, var splitBy: String) {

    STRING_EQUALS("equal(s)?|is", "\"{0}\" == \"{1}\""),

    IS_NUMBER("isNum(ber)?", "TrUtils.isNumber(\"{0}\")"),

    IS_OPERATOR("isOp(erator)?(s)?", "player.isOp()"),

    IS_PLAYER_ONLINE("isOnline", "Bukkit.getPlayerExact(\"{0}\").isOnline()"),

    HAS_PERMISSION("ha(s|ve)(-)?(Perm(ission)?(s)?)", "player.hasPermission(\"{0}\")", "~"),

    HAS_LEVEL("ha(s|ve)(-)?(Level|Lv)(s)?", "layer.getLevel() >= {0}"),

    HAS_MONEY("ha(s|ve)(-)?(Money|Eco|Coin)(s)?", "utils.hasMoney(player, {0})"),

    HAS_POINTS("ha(s|ve)(-)?(Money|Eco|Coin)(s)?", "utils.hasPoints(player, {0})");


    constructor(name: String, replace: String) : this(name, replace, ".")

    constructor(name: String, replace: String, splitBy: String) : this(Regex("(?i)$name."), replace, splitBy)

    fun parse(string: String): String = Strings.replaceWithOrder(replace, *getArguments(string))

    private fun getArguments(string: String): Array<String> = string.split(splitBy).toMutableList().let {
        it.removeAt(0)
        return@let it.toTypedArray()
    }

}