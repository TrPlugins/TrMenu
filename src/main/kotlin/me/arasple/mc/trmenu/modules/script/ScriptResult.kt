package me.arasple.mc.trmenu.modules.script

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/3/29 10:52
 */
class ScriptResult(private val result: Any?, private val throwable: Throwable?) {

    constructor(result: Any?) : this(result, null)
    constructor() : this(null, null)

    fun isSucceed() = throwable == null && result != null

    fun asBoolean() = if (result is Boolean) result else result.toString().equals("yes", true)

    fun asString() = result.toString()

    fun asItemStack() = if (result is ItemStack) result else null

    fun asCollection() = if (result is Collection<*>) result else null

}