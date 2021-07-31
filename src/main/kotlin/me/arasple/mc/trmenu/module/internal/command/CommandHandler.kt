package me.arasple.mc.trmenu.module.internal.command

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.kotlin.Indexed
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.api.receptacle.ReceptacleAPI
import me.arasple.mc.trmenu.api.receptacle.window.type.InventoryChest
import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import me.arasple.mc.trmenu.module.display.texture.Texture
import me.arasple.mc.trmenu.module.internal.command.impl.*
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.item.ItemRepository
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.Time
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import me.arasple.mc.trmenu.util.net.Paster
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemoryConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.*
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XSound
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.sendLang
import taboolib.type.BukkitEquipment
import kotlin.system.measureNanoTime


/**
 * @author Arasple
 * @date 2020/5/30 14:11
 */
@CommandHeader(name = "trmenu", aliases = ["menu"], permission = "trmenu.access")
class CommandHandler {

    /*
     * 暂时做了兼容, 命令框架应当需要大改.
     */
    @CommandBody(permission = "test"/*, description = "Test loaded menus"*/)
    val test = CommandTest.command

    @CommandBody(permission = "trmenu.command.list"/*, description = "List loaded menus"*/)
    val list = CommandList.command

    @CommandBody(permission = "trmenu.command.open"/*, description = "Open a menu for player"*/)
    val open = CommandOpen.command

    @CommandBody(permission = "trmenu.command.reload"/*, description = "Reload all menus"*/)
    val reload = CommandReload.command

    @CommandBody(permission = "trmenu.command.template"/*, description = "Quick layout menu", type = CommandType.PLAYER*/)
    val template = CommandTemplate.command

    @CommandBody(permission = "trmenu.command.action"/*, description = "Run actions for test"*/)
    val action = CommandAction.command

    @CommandBody(permission = "trmenu.command.item"/*, description = "Manipulate items"*/)
    val item = CommandItem.command

    @CommandBody(permission = "trmenu.command.sounds"/*, description = "Preview & test sounds", type = CommandType.PLAYER*/)
    var sounds = CommandSounds.command

    @CommandBody(permission = "trmenu.command.debug"/*, description = "Print debug info"*/)
    val debug = CommandDebug.command
    
}