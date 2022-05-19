package trplugins.menu.module.internal.script.js

import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.data.NetQuery
import trplugins.menu.module.internal.hook.HookPlugin
import trplugins.menu.util.Bungees
import trplugins.menu.util.ClassUtils
import trplugins.menu.util.bukkit.Heads
import trplugins.menu.util.bukkit.ItemMatcher
import me.clip.placeholderapi.PlaceholderAPI
import org.apache.commons.lang3.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.util.random
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.compat.getBalance
import taboolib.platform.util.ItemBuilder
import taboolib.type.BukkitEquipment
import trplugins.menu.TrMenu
import java.util.*


/**
 * @author Arasple
 * @date 2020/7/21 20:57
 */
class Assist {

    companion object {

        val INSTANCE = Assist()

        private val romanNumbers = TreeMap(mapOf(1000 to "M", 900 to "CM", 500 to "D", 400 to "CD", 100 to "C", 90 to "XC", 50 to "L", 40 to "XL", 10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"))

    }

    /**
     * Actions Execute & Control
     */

    fun runAction(player: Player, vararg actions: String) {
        TrMenu.actionHandle.runAction(adaptPlayer(player), actions.filter { it.isNotBlank() })
    }

    fun runAction(player: Player, actions: List<String>) {
        TrMenu.actionHandle.runAction(adaptPlayer(player), actions.filter { it.isNotBlank() })
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
            BukkitEquipment.getItems(this)[BukkitEquipment.fromNMS(equipmentSlot)]
        }
    }

    fun hasEquipment(player: String, equipmentSlot: String): Boolean {
        val item = getEquipment(player, equipmentSlot)
        return !(item == null || item.type == Material.AIR)
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
        return ItemBuilder(XMaterial.STONE)
    }

    fun getTellraw(): TellrawJson {
        return TellrawJson()
    }

    fun getItemName(itemStack: ItemStack): String? {
        return if (itemStack.type != Material.AIR && itemStack.itemMeta?.hasDisplayName() == true)
            itemStack.itemMeta?.displayName
        else
            itemStack.getI18nName()
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


    fun getData(player: Player): MutableMap<String, Any?> {
        return Metadata.getData(player).data
    }

    fun getMeta(player: Player): MutableMap<String, Any?> {
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
        return player.getBalance() >= toDouble(money)
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
        return random(toDouble(number))
    }

    fun randomInteger(low: Int, high: Int): Int {
        return IntRange(low, high).random()
    }

    fun randomDouble(low: Double, high: Double): Double {
        return random(low, high)
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

    fun toRoman(number: String): String {
        return toRoman(toInt(number))
    }

    fun toRoman(number: Int): String {
        if (number < 1) return ""
        val mapNumber = romanNumbers.floorKey(number)
        return if (mapNumber == number) romanNumbers[number]!! else romanNumbers[mapNumber] + toRoman(number - mapNumber)
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
    
    fun isIncludes(input1: String, input2: String): Boolean {
        return input1.contains(input2)
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

    fun staticClass(className: String): Any? {
        return ClassUtils.staticClass(className)
    }

    fun join(separator: String, list: List<Any>): String {
        return list.joinToString(separator)
    }


    /**
     * NBT
     */

    fun getNBT(itemStack: ItemStack, string: String): String? {
        val itemTag = itemStack.getItemTag()
        return itemTag[string]?.asString()
    }

    fun setNBT(itemStack: ItemStack, key: String, value: String): ItemStack {
        val itemTag = itemStack.getItemTag()
        itemTag[key] = ItemTagData(value)
        return itemStack.also { itemTag.saveTo(it) }
    }

    fun getLore(itemStack: ItemStack): MutableList<String> {
        return itemStack.itemMeta?.lore ?: mutableListOf("0")
    }

    fun setLore(itemStack: ItemStack, loreList: MutableList<String>): MutableList<String> {
        itemStack.itemMeta?.lore = loreList
        return itemStack.itemMeta?.lore ?: mutableListOf("0")
    }
}
