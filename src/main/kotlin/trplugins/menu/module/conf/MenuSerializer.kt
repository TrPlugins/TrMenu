package trplugins.menu.module.conf

import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemFlag
import taboolib.common.platform.function.pluginId
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import trplugins.menu.TrMenu
import trplugins.menu.TrMenu.actionHandle
import trplugins.menu.api.menu.ISerializer
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.api.receptacle.ReceptacleClickType
import trplugins.menu.api.suffixes
import trplugins.menu.module.conf.prop.SerializeError
import trplugins.menu.module.conf.prop.SerialzeResult
import trplugins.menu.module.display.Menu
import trplugins.menu.module.display.MenuSettings
import trplugins.menu.module.display.icon.Icon
import trplugins.menu.module.display.icon.IconProperty
import trplugins.menu.module.display.icon.Position
import trplugins.menu.module.display.item.Item
import trplugins.menu.module.display.item.Lore
import trplugins.menu.module.display.item.Meta
import trplugins.menu.module.display.layout.Layout
import trplugins.menu.module.display.layout.MenuLayout
import trplugins.menu.module.display.texture.Texture
import trplugins.menu.module.internal.script.js.ScriptFunction
import trplugins.menu.util.bukkit.ItemMatcher
import trplugins.menu.util.collections.CycleList
import trplugins.menu.util.collections.IndivList
import trplugins.menu.util.conf.Property
import trplugins.menu.util.parseIconId
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
        val id = file.nameWithoutExtension
        val result = SerialzeResult(SerialzeResult.Type.MENU)
        // 文件格式检测
        if (!Type.values().any { it.suffixes.any { file.extension.equals(it, true) } }) {
            result.state = SerialzeResult.State.IGNORE
            return result
        }
        // 文件有效检测
        if (!(file.isFile && file.length() > 0 && file.canRead())) {
            result.submitError(SerializeError.INVALID_FILE, file.name)
            return result
        }
        // 菜单类型
        val type = Type.values().find { it.suffixes.any { file.extension.equals(it, true) } }!!
        // 加载菜单配置
        val conf = Configuration.loadFromFile(file, type)

        // 读取菜单设置
        val settings = serializeSetting(conf)
        if (!settings.succeed()) {
            result.errors.addAll(settings.errors).also {
                return result
            }
        }

        // 读取菜单布局
        val layout = serializeLayout(conf)
        if (!layout.succeed()) {
            result.errors.addAll(layout.errors).also { return result }
        }
        // 读取菜单图标
        val icons = serializeIcons(conf, layout.asLayout())
        if (!icons.succeed()) {
            result.errors.addAll(icons.errors).also {
                return result
            }
        }
        // 返回菜单
        Menu(id, settings.result as MenuSettings, layout.result as MenuLayout, icons.asIcons(), conf).also {
            result.result = it
            return result
        }
    }

    /**
     * Ⅱ. 载入菜单设置 MenuSettings
     */
    override fun serializeSetting(conf: Configuration): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.MENU_SETTING)
        val options = Property.OPTIONS.ofSection(conf)
        val bindings = Property.BINDINGS.ofSection(conf)
        val events = Property.EVENTS.ofSection(conf)
        val tasks = Property.TASKS.ofMap(conf, true)
        val funs = Property.FUNCTIONS.ofMap(conf, true)
        val title = Property.TITLE.ofStringList(conf, listOf(pluginId))
        val titleUpdate = Property.TITLE_UPDATE.ofInt(conf, -20)
        val optionEnableArguments = Property.OPTION_ENABLE_ARGUMENTS.ofBoolean(options, true)
        val optionDefaultArguments = Property.OPTION_DEFAULT_ARGUMENTS.ofStringList(options)
        val optionFreeSlots = Property.OPTION_FREE_SLOTS.ofStringList(conf)
        val optionDefaultLayout = Property.OPTION_DEFAULT_LAYOUT.ofInt(options, 0)
        val optionHidePlayerInventory = Property.OPTION_HIDE_PLAYER_INVENTORY.ofBoolean(options, false)
//        val optionHidePurePacket = Property.OPTION_PURE_PACKET.ofBoolean(options, true)
        val optionMinClickDelay = Property.OPTION_MIN_CLICK_DELAY.ofInt(options, 200)
        val optionDependExpansions = Property.OPTION_DEPEND_EXPANSIONS.ofStringList(options)
        val boundCommands = Property.BINDING_COMMANDS.ofStringList(bindings)
        val boundItems = Property.BINDING_ITEMS.ofStringList(bindings)
        val eventOpen = Property.EVENT_OPEN.ofList(events)
        val eventClose = Property.EVENT_CLOSE.ofList(events)
        val eventClick = Property.EVENT_CLICK.ofList(events)

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
            boundCommands.map { it.toRegex() },
            boundItems.map { ItemMatcher.of(it) }.toTypedArray(),
            Reactions.ofReaction(actionHandle, eventOpen),
            Reactions.ofReaction(actionHandle, eventClose),
            Reactions.ofReaction(actionHandle, eventClick),
            mutableMapOf<Long, Reactions>().run {
                tasks.forEach { (_, content) ->
                    val map = Property.asSection(content)
                    val period = Property.PERIOD.ofInt(map, -1)
                    val reactions = Reactions.ofReaction(actionHandle, Property.TASKS.ofList(map))
                    if (period > 0 && !reactions.isEmpty()) this[period.toLong()] = reactions
                }
                this
            },
            funs.map { ScriptFunction(it.key, it.value.toString()) }.toSet()
        )
        return result
    }

    /**
     * Ⅲ. 载入布局功能 Layout
     */
    override fun serializeLayout(conf: Configuration): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.MENU_LAYOUT)
        val layouts = mutableListOf<Layout>()
        val layout = Property.LAYOUT.ofLists(conf)
        val playerInventory = Property.LAYOUT_PLAYER_INVENTORY.ofLists(conf)
        val inventoryType = Property.INVENTORY_TYPE.ofString(conf, "CHEST")
        val bukkitType = InventoryType.values().find { it.name.equals(inventoryType, true) } ?: InventoryType.CHEST
        val rows = Property.SIZE.ofInt(conf, 0).let {
            if (it > 6) return@let it / 9
            else it
        }

        for (index in 0 until max(layout.size, playerInventory.size)) {
            val lay = arrayOf(layout.getOrElse(index) { listOf() }, playerInventory.getOrElse(index) { listOf() })
            layouts += Layout(rows, bukkitType, lay[0], lay[1])
        }
        if (layouts.isEmpty()) {
            val lay = arrayOf(listOf<String>(), listOf())
            layouts += Layout(rows, bukkitType, lay[0], lay[1])
        }

        result.result = MenuLayout(layouts.toTypedArray())
        return result
    }

    /**
     * Ⅳ. 载入图标功能 Icons
     */
    override fun serializeIcons(conf: Configuration, layout: MenuLayout): SerialzeResult {
        val result = SerialzeResult(SerialzeResult.Type.ICON)
        val icons = Property.ICONS.ofMap(conf).map { (id, value) ->
            val section = Property.asSection(value).let {
                if (it !is Configuration) return@let null
                return@let Configuration.loadFromString(it.saveToString().split("\n").joinToString("\n") {
//                    VariableReader("@", "@")
                    it.parseIconId(id)
                }, conf.type)
            }

            val refresh = Property.ICON_REFRESH.ofInt(section, -1)
            val update = Property.ICON_UPDATE.ofIntList(section)
            val display = Property.ICON_DISPLAY.ofSection(section)
            val action = Property.ACTIONS.ofSection(section)
            val defIcon = loadIconProperty(id, null, section, display, action, -1)
            val slots = Property.ICON_DISPLAY_SLOT.ofLists(display)
            var pages = Property.ICON_DISPLAY_PAGE.ofIntList(display)
            var order = 0
            val search = layout.search(id, pages)

            val position =
                if (slots.isNotEmpty()) {
                    val slot = CycleList(slots.map { Position.Slot.from(it) })
                    if (pages.isEmpty()) pages = pages.plus(0)
                    Position(pages.associateWith { slot })
                } else Position(search.mapValues { CycleList(Position.Slot.from(it.value)) })

            val subs = Property.ICON_SUB_ICONS.ofList(section).map {
                val sub = Property.asSection(it)
                val subDisplay = Property.ICON_DISPLAY.ofSection(sub)
                val subAction = Property.ACTIONS.ofSection(sub)
                loadIconProperty(id, defIcon, sub, subDisplay, subAction, order++)
            }.sortedBy { it.priority }

            if (defIcon.display.texture.isEmpty() || subs.any { it.display.texture.isEmpty() }) {
                result.submitError(SerializeError.INVALID_ICON_UNDEFINED_TEXTURE, id)
            }

            Icon(id, refresh.toLong(), update.toTypedArray(), position, defIcon, IndivList(subs))
        }

        result.result = icons

        return result
    }

    /**
     * Func Ⅴ. 载入图标显示部分
     */
    private val loadIconProperty: (String, IconProperty?, Configuration?, Configuration?, Configuration?, Int) -> IconProperty =
        { id, def, it, display, action, order ->
            val name = Property.ICON_DISPLAY_NAME.ofStringList(display)
            val texture = Property.ICON_DISPLAY_MATERIAL.ofStringList(display)
            val lore = Property.ICON_DISPLAY_LORE.ofLists(display)
            val amount = Property.ICON_DISPLAY_AMOUNT.ofString(display, "1")
            val shiny = Property.ICON_DISPLAY_SHINY.ofString(display, "false")
            val flags = Property.ICON_DISPLAY_FLAGS.ofStringList(display)
            val nbt = Property.ICON_DISPLAY_NBT.ofMap(display)
            // only for the subIcon
            val priority = Property.PRIORITY.ofInt(it, order)
            val condition = Property.CONDITION.ofString(it, "")
            val inherit = Property.INHERIT.ofBoolean(it, false)
            val clickActions = mutableMapOf<Set<ReceptacleClickType>, Reactions>()
            action?.getValues(false)?.forEach { (type, reaction) ->
                val clickTypes = ReceptacleClickType.matches(type)
                if (clickTypes.isNotEmpty()) {
                    val reactions = Reactions.ofReaction(actionHandle, reaction)
                    if (!reactions.isEmpty()) clickActions[clickTypes] = Reactions.ofReaction(actionHandle, reaction)
                }
            }

            val item = Item(
                // 图标材质
                if (def != null && texture.isEmpty()) def.display.texture
                else CycleList(texture.map { Texture.createTexture(it) }),
                // 图标显示名称
                if (def != null && inherit && name.isEmpty()) def.display.name
                else CycleList(name),
                // 图标显示描述
                if (def != null && inherit && lore.isEmpty()) def.display.lore
                else CycleList(lore.map { Lore(line(it)) }),
                // 图标附加属性
                Meta(amount, shiny,
                    flags.mapNotNull { flag ->
                        ItemFlag.values().find { it.name.equals(flag, true) }
                    }.toTypedArray(),
                    ItemTag().also { nbt.forEach { (key, value) -> it[key] = ItemTagData.toNBT(value) } }
                )
            )
            IconProperty(priority, condition, item, clickActions)
        }

    val line: (List<String>) -> List<String> =
        { origin -> mutableListOf<String>().also { list -> origin.forEach { list.addAll(it.split("\n")) } } }


}