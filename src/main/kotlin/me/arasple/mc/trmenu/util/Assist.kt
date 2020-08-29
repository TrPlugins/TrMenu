package me.arasple.mc.trmenu.util

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trmenu.api.Extends.getArguments
import me.arasple.mc.trmenu.api.Extends.getMeta
import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.modules.function.WebData
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import me.arasple.mc.trmenu.modules.function.hook.impl.HookLuckPerms
import me.arasple.mc.trmenu.modules.function.item.ItemIdentifierHandler
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * @author Arasple
 * @date 2020/7/21 20:57
 */
class Assist {

    fun runAction(player: Player, vararg actions: String) {
        actions.filter { it.isNotBlank() }.forEach { Actions.runCachedAction(player, it) }
    }

    fun parsePlaceholders(player: OfflinePlayer, string: String): String {
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    fun parseBracketPlaceholders(player: OfflinePlayer, string: String): String {
        return PlaceholderAPI.setBracketPlaceholders(player, string)
    }

    fun connect(player: Player, server: String) {
        Bungees.connect(player, server)
    }

    fun sendBungeeData(player: Player, vararg data: String) {
        Bungees.sendBungeeData(player, *data)
    }

    fun getPlayerArgs(player: String): Array<String> {
        return getPlayer(player)?.getArguments() ?: arrayOf()
    }

    fun isPlayerOperator(player: String): Boolean {
        return getPlayer(player).let { return@let it != null && it.isOp }
    }

    fun isPlayerOnline(player: String): Boolean {
        return getPlayer(player).let { return@let it != null && it.isOnline }
    }

    fun isPlayerWhitelisted(player: String): Boolean {

        return Bukkit.getWhitelistedPlayers().any { it.name.equals(player, true) }
    }

    fun addWhitelist(player: String): Boolean {
        getOfflinePlayer(player)?.let {
            Bukkit.getWhitelistedPlayers().add(it)
            return true
        }
        return false
    }

    fun removeWhitelist(player: String): Boolean {
        return Bukkit.getWhitelistedPlayers().removeIf { it.name.equals(player, true) }
    }

    fun getPlayer(player: String): Player? {
        return Bukkit.getPlayerExact(player)
    }

    fun getOfflinePlayer(player: String): OfflinePlayer? {
        return Bukkit.getOfflinePlayers().firstOrNull { it.name.equals(player, true) }
    }

    fun getOnlinePlayers(): List<Player> {
        return Bukkit.getOnlinePlayers().sortedBy { it.name }
    }

    @ExperimentalStdlibApi
    fun getRandomPlayer(): Player? {
        return Bukkit.getOnlinePlayers().randomOrNull()
    }

    fun getItemBuildr(): ItemBuilder {
        return ItemBuilder(Material.STONE)
    }

    fun getTellraw(): TellrawJson {
        return TellrawJson.create()
    }

    fun emptyString(length: Int): String {
        return buildString {
            for (i in 0..length) append(" ")
        }
    }

    fun equalsIgnoreCase(sample: String, temp: String): Boolean {
        return sample.equals(temp, true)
    }

    fun chance(number: String): Boolean {
        return Numbers.random(NumberUtils.toDouble(number, 0.0))
    }

    fun randomInteger(low: Int, high: Int): Int {
        return IntRange(low, high).random()
    }

    fun randomDouble(low: Double, high: Double): Double {
        return Numbers.getRandomDouble(low, high)
    }

    fun isNumber(number: String): Boolean {
        return NumberUtils.isParsable(number)
    }

    fun isInt(number: String) = try {
        number.toInt()
        true
    } catch (e: Throwable) {
        false
    }

    fun toInt(number: String) = NumberUtils.toInt(number, -1)

    fun toDouble(number: String) = NumberUtils.toDouble(number, -1.0)

    fun isWithin(input: String, low: String, high: String) = IntRange(low.toInt(), high.toInt()).contains(input.toInt())

    fun isSmaller(input1: String, input2: String) = NumberUtils.toDouble(input1) < NumberUtils.toDouble(input2)

    fun isSmallerOrEqual(input1: String, input2: String) = NumberUtils.toDouble(input1) <= NumberUtils.toDouble(input2)

    fun isGreater(input1: String?, input2: String?) = NumberUtils.toDouble(input1) > NumberUtils.toDouble(input2)

    fun isGreaterOrEqual(input1: String?, input2: String?) =
        NumberUtils.toDouble(input1) >= NumberUtils.toDouble(input2)

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

    fun hasMoney(player: Player, money: Double) = HookInstance.get(player) >= money

    fun hasPoints(player: Player, points: String) = hasPoints(player, NumberUtils.toInt(points, 0))

    fun hasPoints(player: Player, points: Int) = HookInstance.getPlayerPoints().hasPoints(player, points)

    fun getItemName(itemStack: ItemStack): String = Items.getName(itemStack)

    fun query(url: String) = WebData.query(url)

    fun hasItem(player: String, identify: String) =
        getPlayer(player)?.let { ItemIdentifierHandler.read(identify).hasItem(it) } ?: false

    fun hasItem(player: Player, identify: String) = ItemIdentifierHandler.read(identify).hasItem(player)

    fun hasMeta(player: Player, id: String) = player.getMeta(id) != null

    fun evalCronusCondition(player: String, condition: String) =
        getPlayer(player)?.let { return@let evalCronusCondition(it, condition) } ?: false

    fun evalCronusCondition(player: Player, condition: String) =
        HookInstance.getCronus().parseCondition(condition).check(player)

    fun getPlayerHead(name: String) = Skulls.getPlayerHead(name)

    fun getLuckPerms() = HookInstance.get("LuckPerms") as HookLuckPerms

    companion object {

        val INSTANCE = Assist()

    }


}