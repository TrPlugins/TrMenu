package me.arasple.mc.trmenu.modules.script.utils

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.compat.EconomyHook
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.data.MetaPlayer.getArguments
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.hook.HookCronus
import me.arasple.mc.trmenu.modules.hook.HookPlayerPoints
import me.arasple.mc.trmenu.modules.web.WebData
import me.arasple.mc.trmenu.utils.Bungees
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * @author Arasple
 * @date 2020/7/21 20:57
 */
class AssistUtils {

    fun runAction(player: Player, vararg actions: String) = actions.filter { it.isNotBlank() }.forEach { Actions.runCachedAction(player, it) }

    fun parseBracketPlaceholders(player: OfflinePlayer, string: String): String = PlaceholderAPI.setBracketPlaceholders(player, string)

    fun connect(player: Player, server: String) = Bungees.connect(player, server)

    fun sendBungeeData(player: Player, vararg data: String) = Bungees.sendBungeeData(player, *data)

    fun getPlayerArgs(player: String): Array<String> = getPlayer(player)?.getArguments() ?: arrayOf()

    fun isPlayerOperator(player: String) = getPlayer(player).let { return@let it != null && it.isOp }

    fun isPlayerOnline(player: String) = getPlayer(player).let { return@let it != null && it.isOnline }

    fun getPlayer(player: String): Player? = Bukkit.getPlayerExact(player)

    fun getOnlinePlayers(): Collection<Player> = Bukkit.getOnlinePlayers()

    @ExperimentalStdlibApi
    fun getRandomPlayer(): Player? = Bukkit.getOnlinePlayers().randomOrNull()

    fun getItemBuildr(): ItemBuilder = ItemBuilder(Material.STONE)

    fun getTellraw(): TellrawJson = TellrawJson.create()

    fun emptyString(length: Int) = buildString {
        for (i in 0..length) append(" ")
    }

    fun equalsIgnoreCase(sample: String, temp: String) = sample.equals(temp, true)

    fun chance(number: String) = Numbers.random(NumberUtils.toDouble(number, 0.0))

    fun randomInteger(low: Int, high: Int): Int = IntRange(low, high).random()

    fun randomDouble(low: Double, high: Double) = Numbers.getRandomDouble(low, high)

    fun isNumber(number: String) = NumberUtils.isParsable(number)

    fun toInt(number: String) = NumberUtils.toInt(number, -1)

    fun toDouble(number: String) = NumberUtils.toDouble(number, -1.0)

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

    fun hasMoney(player: Player, money: String) = hasMoney(player, NumberUtils.toDouble(money, 0.0))

    fun hasMoney(player: Player, money: Double) = EconomyHook.get(player) >= money

    fun hasPoints(player: Player, points: String) = hasPoints(player, NumberUtils.toInt(points, 0))

    fun hasPoints(player: Player, points: Int) = HookPlayerPoints.hasPoints(player, points)

    fun getItemName(itemStack: ItemStack): String = Items.getName(itemStack)

    fun readWebData(url: String) = WebData.query(url)

    fun evalCronusCondition(player: String, condition: String) = getPlayer(player)?.let { return@let evalCronusCondition(it, condition) } ?: false

    fun evalCronusCondition(player: Player, condition: String) = HookCronus.parseCondition(condition).check(player)

    companion object {

        val INSTANCE = AssistUtils()

    }


}