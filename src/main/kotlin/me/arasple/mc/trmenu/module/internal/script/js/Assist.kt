package me.arasple.mc.trmenu.module.internal.script.js

import io.izzel.taboolib.common.plugin.bridge.BridgeImpl
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.kotlin.Randoms
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.item.Equipments
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.data.NetQuery
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.util.Bungees
import me.arasple.mc.trmenu.util.bukkit.Heads
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory


/**
 * @author Arasple
 * @date 2020/7/21 20:57
 */
class Assist {

    companion object {

        val INSTANCE = Assist()

    }

    /**
     * Actions Execute & Control
     */

    fun runAction(player: Player, vararg actions: String) {
        Actions.runAction(player, actions.filter { it.isNotBlank() })
    }


    /**
     * Bukkit
     */

    fun isPlayerOperator(player: String): Boolean {
        return getPlayer(player)?.isOp ?: false
    }

    fun isPlayerOnline(player: String): Boolean {
        return getPlayer(player)?.isOnline ?: false
    }

    fun isPlayerWhitelisted(player: String): Boolean {
        return getOfflinePlayer(player)?.isWhitelisted ?: false
    }

    fun addWhitelist(player: String): Boolean {
        return getOfflinePlayer(player).let {
            Bukkit.getWhitelistedPlayers().add(it)
            true
        }
    }

    fun removeWhitelist(player: String): Boolean {
        return Bukkit.getWhitelistedPlayers().removeIf { it.name.equals(player, true) }
    }

    fun getPlayer(player: String): Player? {
        return Bukkit.getPlayerExact(player)
    }

    fun getOfflinePlayer(player: String): OfflinePlayer? {
        return Bukkit.getOfflinePlayers().find { it.name.equals(player, true) }
    }

    fun getOnlinePlayers(): List<Player> {
        return Bukkit.getOnlinePlayers().sortedBy { it.name }
    }

    fun getRandomPlayer(): Player? {
        return Bukkit.getOnlinePlayers().randomOrNull()
    }

    fun getPlayerInventory(player: String): PlayerInventory? {
        return getPlayer(player)?.inventory
    }

    fun getArmorContents(player: String): Array<ItemStack>? {
        return getPlayerInventory(player)?.armorContents
    }

    fun getItemInHand(player: String, offhand: Boolean = false): ItemStack? {
        return getPlayerInventory(player)?.let {
            if (offhand) it.itemInOffHand
            else it.itemInMainHand
        }
    }

    // utils.getEquipment("Arasple", "HEAD")
    fun getEquipment(player: String, equipmentSlot: String): ItemStack? {
        return getPlayer(player)?.run {
            Equipments.getItems(this)[Equipments.fromNMS(equipmentSlot)]
        }
    }

    fun hasEquipment(player: String, equipmentSlot: String): Boolean {
        return !Items.isNull(getEquipment(player, equipmentSlot))
    }


    /**
     * BungeeCord
     */

    fun connect(player: Player, server: String) {
        Bungees.connect(player, server)
    }

    fun sendBungeeData(player: Player, vararg data: String) {
        Bungees.sendBungeeData(player, *data)
    }

    /**
     * Internal - TrMenu
     */

    fun getPlayerArgs(player: String): Array<String>? {
        return getSession(player)?.arguments
    }

    fun query(url: String): NetQuery {
        return NetQuery.query(url, 60 * 3)
    }

    fun query(url: String, span: Int = 60 * 3): NetQuery {
        return NetQuery.query(url, span)
    }

    fun getSession(player: String): MenuSession? {
        return getPlayer(player)?.let { MenuSession.getSession(it) }
    }


    /**
     * Internal - TabooLib Utils
     */

    fun getItemBuildr(): ItemBuilder {
        return ItemBuilder(Material.STONE)
    }

    fun getTellraw(): TellrawJson {
        return TellrawJson.create()
    }

    fun getItemName(itemStack: ItemStack): String {
        return Items.getName(itemStack)
    }

    fun hasItem(player: String, identify: String): Boolean {
        return hasItem(getPlayer(player), identify)
    }

    fun hasItem(player: Player?, identify: String): Boolean {
        return player?.let { ItemMatcher.of(identify).hasItem(it) } ?: false
    }

    fun listData(player: Player): List<String> {
        return Metadata.getData(player).data.keys.sorted()
    }


    fun getData(player: Player): MutableMap<String, Any> {
        return Metadata.getData(player).data
    }

    fun getMeta(player: Player): MutableMap<String, Any> {
        return Metadata.getMeta(player).data
    }

    /**
     * Hook - PlaceholderAPI
     */

    fun parsePlaceholders(player: OfflinePlayer, string: String): String {
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    fun parseBracketPlaceholders(player: OfflinePlayer, string: String): String {
        return PlaceholderAPI.setBracketPlaceholders(player, string)
    }


    /**
     * Hook - Economy
     */

    fun hasMoney(player: Player, money: String): Boolean {
        BridgeImpl.handle()
        return hasMoney(player, toDouble(money))
    }

    fun hasMoney(player: Player, money: Double): Boolean {
        return HookPlugin.getVault().hasMoney(player, money)
    }

    fun hasPoints(player: Player, points: String): Boolean {
        return hasPoints(player, toInt(points))
    }

    fun hasPoints(player: Player, points: Int): Boolean {
        return HookPlugin.getPlayerPoints().hasPoints(player, points)
    }

    fun getHead(name: String): ItemStack {
        return Heads.getHead(name)
    }

    /**
     * Numbers
     */
    fun chance(number: String): Boolean {
        return Randoms.random(toDouble(number))
    }

    fun randomInteger(low: Int, high: Int): Int {
        return IntRange(low, high).random()
    }

    fun randomDouble(low: Double, high: Double): Double {
        return Randoms.random(low, high)
    }

    fun isNumber(number: String): Boolean {
        return NumberUtils.isParsable(number)
    }

    fun isInt(number: String): Boolean {
        return try {
            number.toInt()
            true
        } catch (e: Throwable) {
            false
        }
    }

    fun toInt(number: String): Int {
        return number.toIntOrNull() ?: 0
    }

    fun toDouble(number: String, def: Double = 0.0): Double {
        return number.toDoubleOrNull() ?: def
    }

    fun isWithin(input: String, low: String, high: String): Boolean {
        return (low.toInt()..high.toInt()).contains(input.toInt())
    }

    fun isSmaller(input1: String, input2: String): Boolean {
        return toDouble(input1) < toDouble(input2)
    }

    fun isSmallerOrEqual(input1: String, input2: String): Boolean {
        return toDouble(input1) <= toDouble(input2)
    }

    fun isGreater(input1: String, input2: String): Boolean {
        return toDouble(input1) > toDouble(input2)
    }

    fun isGreaterOrEqual(input1: String, input2: String): Boolean {
        return toDouble(input1) >= toDouble(input2)
    }

    /**
     * Hook
     */
    fun isBedrockPlayer(player: Player): Boolean {
        return HookPlugin.getFloodgate().isBedrockPlayer(player)
    }


    /**
     * Miscellaneous
     */

    fun emptyString(length: Int): String {
        return buildString { for (i in 0..length) append(" ") }
    }


    fun equalsIgnoreCase(sample: String, temp: String): Boolean {
        return sample.equals(temp, true)
    }

    fun sort(list: List<Any>): List<Any> {
        return list.sortedBy { it.toString() }
    }

}