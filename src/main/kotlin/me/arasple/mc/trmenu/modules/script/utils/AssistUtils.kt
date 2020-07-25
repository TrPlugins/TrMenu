package me.arasple.mc.trmenu.modules.script.utils

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.data.MetaPlayer.getArguments
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.utils.Bungees
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/21 20:57
 */
class AssistUtils {

    fun runAction(player: Player, vararg actions: String) = actions.filter { it.isNotBlank() }.forEach { Actions.runCachedAction(player, it) }

    fun parseBracketPlaceholders(player: Player, string: String): String = PlaceholderAPI.setBracketPlaceholders(player, string)

    fun connect(player: Player, server: String) = Bungees.connect(player, server)

    fun sendBungeeData(player: Player, vararg data: String) = Bungees.sendBungeeData(player, *data)

    fun getPlayerArgs(player: String): Array<String> = getPlayer(player)?.getArguments() ?: arrayOf()

    fun isPlayerOperator(player: String) = getPlayer(player).let { return@let it != null && it.isOp }

    fun isPlayerOnline(player: String) = getPlayer(player).let { return@let it != null && it.isOnline }

    fun getPlayer(player: String): Player? = Bukkit.getPlayerExact(player)

    @ExperimentalStdlibApi
    fun getRandomPlayer(): Player? = Bukkit.getOnlinePlayers().randomOrNull()

    fun getItemBuildr(): ItemBuilder = ItemBuilder(Material.STONE)

    fun getTellraw(): TellrawJson = TellrawJson.create()

    fun randomInteger(low: Int, high: Int): Int = IntRange(low, high).random()

    fun randomDouble(low: Double, high: Double) = Numbers.getRandomDouble(low, high)

    fun isNumber(number: String) = NumberUtils.isParsable(number)

    fun isWithin(input: String, low: String, high: String) = IntRange(low.toInt(), high.toInt()).contains(input.toInt())

    fun isSmaller(input1: String, input2: String) = NumberUtils.toDouble(input1) < NumberUtils.toDouble(input2)

    fun isSmallerOrEqual(input1: String, input2: String) = NumberUtils.toDouble(input1) <= NumberUtils.toDouble(input2)

    fun isGreater(input1: String?, input2: String?) = NumberUtils.toDouble(input1) > NumberUtils.toDouble(input2)

    fun isGreaterOrEqual(input1: String?, input2: String?) = NumberUtils.toDouble(input1) >= NumberUtils.toDouble(input2)

    fun createLocation(world: String?, x: Double, z: Double): Location? {
        val y = Bukkit.getWorld(world!!)!!.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
        return createLocation(world, x, y, z)
    }

    fun createLocation(world: String?, x: Double, y: Double, z: Double): Location? {
        return createLocation(world, x, y, z, 0f, 0f)
    }

    fun createLocation(world: String?, x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location? {
        return Location(Bukkit.getWorld(world!!), x, y, z, yaw, pitch)
    }

    fun createLocation(world: World, x: Double, z: Double): Location? {
        val y = world.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
        return createLocation(world, x, y, z)
    }

    fun createLocation(world: World?, x: Double, y: Double, z: Double): Location? {
        return createLocation(world, x, y, z, 0f, 0f)
    }

    fun createLocation(world: World?, x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location? {
        return Location(world, x, y, z, yaw, pitch)
    }


    companion object {

        val INSTANCE = AssistUtils()

    }

}