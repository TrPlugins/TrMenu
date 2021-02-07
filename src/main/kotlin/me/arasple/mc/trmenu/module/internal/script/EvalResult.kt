package me.arasple.mc.trmenu.module.internal.script

import me.arasple.mc.trmenu.util.Regexs
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/31 11:53
 */
inline class EvalResult(private val any: Any?) {

    fun asBoolean(def: Boolean = false): Boolean {
        return when (any) {
            is Boolean -> any
            is String -> Regexs.parseBoolean(any)
            else -> def || Regexs.parseBoolean(any.toString())
        }
    }

    fun asItemStack(): ItemStack {
        return any as ItemStack
    }

    fun asString(): String {
        return any.toString()
    }

    companion object {

        val TRUE = EvalResult(true)

    }

}
