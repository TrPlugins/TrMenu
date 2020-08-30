@file:Suppress("NAME_SHADOWING")

package me.arasple.mc.trmenu.modules.conf.serialize

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.conf.menu.MenuConfiguration
import me.arasple.mc.trmenu.modules.conf.property.Property
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.function.InternalFunction
import me.arasple.mc.trmenu.modules.display.function.Reactions
import me.arasple.mc.trmenu.modules.display.menu.MenuLayout
import me.arasple.mc.trmenu.modules.display.menu.MenuSettings
import me.arasple.mc.trmenu.modules.function.item.ItemIdentifier
import me.arasple.mc.trmenu.modules.function.item.ItemIdentifierHandler
import me.arasple.mc.trmenu.util.Utils
import org.bukkit.event.inventory.InventoryType

/**
 * @author Arasple
 * @date 2020/7/6 14:21
 */
object MenuSerializer {

    fun loadMenu(id: String, configuration: MenuConfiguration): Menu? {
        return try {
            val settings = loadSettings(configuration)
            val layout = loadLayout(configuration)
            val icons = IconSerializer.loadIcons(configuration)

            Menu(id, configuration, settings, layout, icons)
        } catch (e: Throwable) {
            TLocale.sendToConsole("LOADER.ERRORS", id, e.message, e.stackTrace.joinToString("\n"))
            null
        }
    }

    /**
     * 加载菜单设置
     */
    private fun loadSettings(c: MenuConfiguration): MenuSettings {
        val titles = MenuSettings.Titles(
                Animated(Utils.asArray(c.getTitle())),
                Utils.asInt(c.getTitleUpdate(), 0)
        )

        val options = MenuSettings.Options(
                c.getOptionEnableArguments()?.let { it.toString().toBoolean() } ?: true,
                Utils.asArray(c.getOptionDefaultArguments()),
                c.getOptionDefaultLayout().toString(),
                Utils.asBoolean(c.getOptionHidePlayerInventory()),
                Utils.asLong(c.getOptionMinClickDelay(), 200),
                Utils.asArray(c.getOptionDependExpansions())
        )

        val bindings = MenuSettings.Bindings(
                mutableListOf<Regex>().let { regexs ->
                    Utils.asList(c.getBindingCommands()).forEach { regexs.add(it.toRegex()) }
                    return@let regexs
                }.toTypedArray(),

                mutableListOf<ItemIdentifier>().let { itemIds ->
                    Utils.asList(c.getBindingItems()).forEach { itemIds.add(ItemIdentifierHandler.read(it)) }
                    return@let itemIds
                }.toTypedArray()
        )

        val events = MenuSettings.Events(
                ReactionSerializer.serializeReactions(c.getEventOpen()),
                ReactionSerializer.serializeReactions(c.getEventClose()),
                ReactionSerializer.serializeReactions(c.getEventClick())
        )

        val tasks = MenuSettings.ScheduledTasks(
                mutableMapOf<Long, Reactions>().let { map ->
                    val obj = c.getTasks()
                    if (obj != null) {
                        Utils.asSection(obj)?.getValues(false)?.values?.forEach { value ->
                            Utils.asSection(value)?.let { section ->
                                run {
                                    val period = section.getLong(Utils.getSectionKey(section, Property.PERIOD), -1)
                                    if (period > 0) {
                                        val reactions = ReactionSerializer.serializeReactions(section[Utils.getSectionKey(section, Property.TASKS)])
                                        if (!reactions.isEmpty) map[period] = reactions
                                    }
                                }
                            }
                        }
                    }
                    return@let map
                }
        )

        val funs = MenuSettings.Funs(
                mutableSetOf<InternalFunction>().let { funs ->
                    c.getFunctions()?.let { it ->
                        Utils.asSection(it)?.getValues(true)?.entries?.forEach {
                            funs.add(InternalFunction(it.key, it.value.toString()))
                        }
                    }
                    return@let funs
                }
        )

        return MenuSettings(titles, options, bindings, events, tasks, funs)
    }

    /**
     * 加载菜单布局
     */
    private fun loadLayout(c: MenuConfiguration): MenuLayout {
        val size = NumberUtils.toInt(c.getSize().toString(), -1).let {
            if (it > 0) {
                return@let (if (it > 6) (it / 9) else it).coerceAtMost(6).coerceAtLeast(1) * 9
            }
            return@let -1
        }

        val type = c.getInventoryType().let { type -> InventoryType.values().firstOrNull { it.name.equals(type.toString(), true) } ?: InventoryType.CHEST }
        val layout: List<List<String>>? = c.getLayout().let { return@let if (it != null && it is List<*>) Utils.asLists(it) else null }
        val layoutInventory: List<List<String>>? = c.getLayoutInventory().let { return@let if (it != null && it is List<*>) Utils.asLists(it) else null }
        val list = mutableListOf<MenuLayout.Layout>()

        if (layout != null && layout.isNotEmpty()) {
            layout.forEachIndexed { i, l ->
                list.add(MenuLayout.Layout(type, l, if (layoutInventory?.size ?: 0 > i) layoutInventory!![i] else null))
            }
        } else if (size > 0) list.add(MenuLayout.Layout(type, listOf(), if (layoutInventory?.isNotEmpty() == true) layoutInventory[0] else null).setRows(size / 9))

        if (layoutInventory != null && layoutInventory.isNotEmpty() && layoutInventory.size > layout?.size ?: 0) {
            layoutInventory.forEachIndexed { i, l ->
                if (i >= layout?.size ?: 0) {
                    list.add(MenuLayout.Layout(type, null, l))
                }
            }
        }

        return MenuLayout(list)
    }

}