package me.arasple.mc.trmenu.module.conf

import io.izzel.taboolib.module.nms.nbt.NBTBase
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.api.menu.ISerializer
import me.arasple.mc.trmenu.api.receptacle.window.vanilla.ClickType
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.conf.prop.Property.Companion.LIST
import me.arasple.mc.trmenu.module.conf.prop.Property.Companion.LISTS
import me.arasple.mc.trmenu.module.conf.prop.Property.Companion.LIST_ANY
import me.arasple.mc.trmenu.module.conf.prop.Property.Companion.MAP
import me.arasple.mc.trmenu.module.conf.prop.SerialzeError
import me.arasple.mc.trmenu.module.conf.prop.SerialzeResult
import me.arasple.mc.trmenu.module.display.Menu
import me.arasple.mc.trmenu.module.display.MenuSettings
import me.arasple.mc.trmenu.module.display.icon.Icon
import me.arasple.mc.trmenu.module.display.icon.IconProperty
import me.arasple.mc.trmenu.module.display.icon.Position
import me.arasple.mc.trmenu.module.display.item.Item
import me.arasple.mc.trmenu.module.display.item.Lore
import me.arasple.mc.trmenu.module.display.item.Meta
import me.arasple.mc.trmenu.module.display.layout.Layout
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import me.arasple.mc.trmenu.module.display.texture.Texture
import me.arasple.mc.trmenu.module.internal.script.Condition
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import me.arasple.mc.trmenu.util.collections.CycleList
import me.arasple.mc.trmenu.util.collections.IndivList
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemFlag
import java.io.File
import kotlin.math.max

/**
 * @author Arasple
 * @date 2021/1/25 10:18
 */
object MenuSerializer : ISerializer {

    /**
     * Ⅰ. 载入菜单
     */
    override fun serializeMenu(file: File): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.MENU)

        // File valid check
        if (!(file.isFile && file.length() > 0 && file.canRead() && file.extension.equals("yml", true))) {
            result.submitError(SerialzeError.INVALID_FILE, file.name)
            return result
        }
        // Read as map
        val yaml = YamlConfiguration.loadConfiguration(file)
        val conf = Property.toMap(yaml)
        // Load Settings
        val settings = serializeSetting(conf)
        if (!settings.succeed()) {
            result.errors.addAll(settings.errors).also {
                return result
            }
        }
        // Load Layout
        val layout = serializeLayout(conf)
        if (!layout.succeed()) {
            result.errors.addAll(layout.errors).also { return result }
        }
        // Load Icons
        val icons = serializeIcon(conf, layout.asLayout())
        if (!icons.succeed()) {
            result.errors.addAll(icons.errors).also {
                return result
            }
        }
        val menu = Menu(
            file.nameWithoutExtension,
            settings.result as MenuSettings,
            layout.result as MenuLayout,
            icons.asIcons()
        )

        result.result = menu
        return result
    }

    /**
     * Ⅱ. 载入菜单设置 MenuSettings
     */
    override fun serializeSetting(conf: Map<String, Any>): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.MENU_SETTING)
        val options = Property.OPTIONS.of(conf, MAP)
        val bindings = Property.BINDINGS.of(conf, MAP)
        val events = Property.EVENTS.of(conf, MAP)
        // TODO TASKS & INTERNAL FUNCTIONS
        val tasks = Property.TASKS.of(conf, MAP)
        val funs = Property.FUNCTIONS.of(conf, MAP)

        val title = Property.TITLE.of(conf, listOf("TrMenu"))
        val titleUpdate = Property.TITLE_UPDATE.of(conf, -20)
        val optionEnableArguments = Property.OPTION_ENABLE_ARGUMENTS.of(options, false)
        val optionDefaultArguments = Property.OPTION_DEFAULT_ARGUMENTS.of(options, LIST)
        val optionFreeSlots = Property.OPTION_FREE_SLOTS.of(options, LIST)
        val optionDefaultLayout = Property.OPTION_DEFAULT_LAYOUT.of(options, 0)
        val optionHidePlayerInventory = Property.OPTION_HIDE_PLAYER_INVENTORY.of(options, false)
        val optionMinClickDelay = Property.OPTION_MIN_CLICK_DELAY.of(options, 200)
        val optionDependExpansions = Property.OPTION_DEPEND_EXPANSIONS.of(options, LIST)
        val boundCommands = Property.BINDING_COMMANDS.of(bindings, LIST)
        val boundItems = Property.BINDING_ITEMS.of(bindings, LIST)
        val eventOpen = Property.EVENT_OPEN.of(events, LIST)
        val eventClose = Property.EVENT_CLOSE.of(events, LIST)
        val eventClick = Property.EVENT_CLICK.of(events, LIST)

        result.result = MenuSettings(
            CycleList(title),
            titleUpdate,
            optionEnableArguments,
            optionDefaultArguments.toTypedArray(),
            optionFreeSlots.flatMap { Position.Slot.readStaticSlots(it) }.toSet(),
            optionDefaultLayout,
            optionDependExpansions.toTypedArray(),
            optionMinClickDelay,
            optionHidePlayerInventory,
            boundCommands.map { it.toRegex() }.toTypedArray(),
            boundItems.map { ItemMatcher.of(it) }.toTypedArray(),
            Reactions.ofReaction(eventOpen),
            Reactions.ofReaction(eventClose),
            Reactions.ofReaction(eventClick),
            mutableMapOf<Long, Reactions>().run {
                tasks.forEach { (_, content) ->
                    val map = Property.castMap(content)
                    val period = Property.PERIOD.of(map, -1)
                    val reactions = Reactions.ofReaction(Property.TASKS.of(map, LIST))

                    if (period > 0 && !reactions.isEmpty()) {
                        this[period.toLong()] = reactions
                    }
                }
                this
            }
        )
        return result
    }

    /**
     * Ⅲ. 载入布局功能 Layout
     */
    override fun serializeLayout(conf: Map<String, Any>): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.MENU_LAYOUT)

        val layouts = mutableListOf<Layout>()
        val layout = Property.LAYOUT.of(conf, LISTS)
        val playerInventory = Property.LAYOUT_PLAYER_INVENTORY.of(conf, LISTS)
        val inventoryType = Property.INVENTORY_TYPE.of(conf, "CHEST")
        val rows = Property.SIZE.of(conf, 0).let {
            if (it > 6) return@let it / 9
            else it
        }

        val bukkitType =
            InventoryType.values().find { it.name.equals(inventoryType, true) } ?: InventoryType.CHEST

        for (index in 0 until max(layout.size, playerInventory.size)) {
            layouts.add(
                Layout(
                    rows,
                    bukkitType,
                    layout.getOrElse(index) { listOf() },
                    playerInventory.getOrElse(index) { listOf() }
                )
            )
        }

        result.result = MenuLayout(layouts.toTypedArray())
        return result
    }

    /**
     * Ⅳ. 载入图标功能 Icons
     */
    override fun serializeIcon(conf: Map<String, Any>, layout: MenuLayout): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.ICON)
        val icons = Property.ICONS.of(conf, mapOf<String, Map<String, Any>>())

        result.result = icons.map { (id, section) ->
            val refresh = Property.ICON_REFRESH.of(section, -1)
            val update = Property.ICON_UPDATE.of(section, listOf<Int>())
            val display = Property.ICON_DISPLAY.of(section, MAP)
            val action = Property.ACTIONS.of(section, MAP)
            val defIcon = loadIconProperty(null, section, display, action)
            val slots = Property.ICON_DISPLAY_SLOT.of(display, LISTS)
            var pages = Property.ICON_DISPLAY_PAGE.of(display, LIST).mapNotNull { it.toIntOrNull() }
            val search = layout.search(id, pages)

            val position =
                if (slots.isNotEmpty()) {
                    val slot = CycleList(slots.map { Position.Slot.from(it) })
                    if (pages.isEmpty()) pages = pages.plus(0)
                    Position(pages.map { it to slot }.toMap())
                } else Position(search.mapValues { CycleList(Position.Slot.from(it.value)) })

            val subs = Property.ICON_SUB_ICONS.of(section, LIST_ANY).map {
                val sub = Property.castMap(it)
                val subDisplay = Property.ICON_DISPLAY.of(sub, MAP)
                val subAction = Property.ACTIONS.of(sub, MAP)

                loadIconProperty(defIcon, sub, subDisplay, subAction)
            }

            when {
                defIcon.display.texture.isEmpty() || subs.any { it.display.texture.isEmpty() } -> result.submitError(
                    SerialzeError.INVALID_ICON_UNDEFINED_TEXTURE,
                    id
                )
            }

            Icon(id, refresh.toLong(), update.toTypedArray(), position, defIcon, IndivList(subs))
        }

        return result
    }

    /**
     * Func Ⅴ. 载入图标显示部分
     */
    private val loadIconProperty: (IconProperty?, Map<String, Any>, Map<String, Any>, Map<String, Any>) -> IconProperty =
        { def, it, display, action ->

            val name = Property.ICON_DISPLAY_NAME.of(display, LIST)
            val texture = Property.ICON_DISPLAY_MATERIAL.of(display, LIST)
            val lore = Property.ICON_DISPLAY_LORE.of(display, LISTS, true)
            val amount = Property.ICON_DISPLAY_AMOUNT.of(display, "1")
            val shiny = Property.ICON_DISPLAY_SHINY.of(display, "false")
            val flags = Property.ICON_DISPLAY_FLAGS.of(display, LIST)
            val nbt = Property.ICON_DISPLAY_NBT.of(display, MAP)
            // only for the subIcon
            val priority = Property.PRIORITY.of(it, 0)
            val condition = Property.REQUIREMENT.of(it, "")
            val inherit = Property.INHERIT.of(it, false)
            val clickActions = mutableMapOf<Set<ClickType>, Reactions>()
            action.forEach { (type, reaction) ->
                val clickTypes = ClickType.matches(type)
                if (clickTypes.isNotEmpty()) {
                    val reactions = Reactions.ofReaction(reaction)
                    if (!reactions.isEmpty()) clickActions[clickTypes] = Reactions.ofReaction(reaction)
                }
            }

            val item = Item(

                if (def != null && texture.isEmpty()) {
                    def.display.texture
                } else CycleList(texture.map { Texture.createTexture(it) }),

                if (def != null && inherit && name.isEmpty()) {
                    def.display.name
                } else CycleList(name),

                if (def != null && inherit && lore.isEmpty()) {
                    def.display.lore
                } else CycleList(lore.map { Lore(it) }),

                Meta(
                    amount,
                    shiny,
                    flags.mapNotNull { flag ->
                        ItemFlag.values().find {
                            it.name.equals(flag, true)
                        }
                    }.toTypedArray(),
                    NBTCompound().also { nbt.forEach { (key, value) -> it[key] = NBTBase.toNBT(value) } }
                )
            )

            IconProperty(priority, Condition(condition), inherit, item, clickActions)
        }

}