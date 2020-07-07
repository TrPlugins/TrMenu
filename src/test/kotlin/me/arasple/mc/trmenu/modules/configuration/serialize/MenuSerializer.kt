package me.arasple.mc.trmenu.modules.configuration.serialize

import me.arasple.mc.trmenu.display.Icon
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.display.menu.MenuSettings
import me.arasple.mc.trmenu.modules.configuration.menu.MenuConfiguration
import me.arasple.mc.trmenu.modules.configuration.property.Property
import me.arasple.mc.trmenu.modules.item.ItemIdentifier
import me.arasple.mc.trmenu.modules.item.ItemIdentifierHandler
import me.arasple.mc.trmenu.utils.Utils
import java.io.File

/**
 * @author Arasple
 * @date 2020/7/6 14:21
 */
object MenuSerializer {

    fun loadMenu(file: File): Menu {
        val id = file.name.split(".", limit = 2)[0]
        val configuration = MenuConfiguration()
        configuration.load(file)

        val settings = loadSettings(configuration)
        val layout = loadLayout(configuration)
        val icons = loadIcons(configuration)

        return Menu(id, configuration, settings, layout, icons)
    }

    private fun loadSettings(c: MenuConfiguration): MenuSettings {
        val titles = MenuSettings.Titles(
            Animated(Utils.asArray(c.getTitle())),
            Utils.asInt(c.getTitleUpdate(), 0)
        )

        val options = MenuSettings.Options(
            Utils.asArray(c.getOptionDefaultArguments()),
            Utils.asBoolean(c.getOptionHidePlayerInventory()),
            Utils.asLong(c.getOptionMinClickDelay(), 200),
            Utils.asArray(c.getOptionDependExpansions())
        )


        val bindings = MenuSettings.Bindings(
            mutableListOf<Regex>().let { regexs ->
                Utils.asArray(c.getBindingCommands()).forEach { regexs.add(it.toRegex()) }
                return@let regexs
            }.toTypedArray(),

            mutableListOf<ItemIdentifier>().let { itemIds ->
                Utils.asArray(c.getBindingItems()).forEach { itemIds.add(ItemIdentifierHandler.read(it)) }
                return@let itemIds
            }.toTypedArray()
        )

        val events = MenuSettings.Events(
            ReactionSerializer.serializeReactions(c.getEventOpen()),
            ReactionSerializer.serializeReactions(c.getEventClose()),
            ReactionSerializer.serializeReactions(c.getEventClick())
        )

        val tasks = MenuSettings.Tasks(
            mutableMapOf<Long, Reactions>().let { map ->
                val obj = c.getTasks()
                if (obj != null) {
                    Utils.asSection(obj)?.getValues(false)?.values?.forEach { value ->
                        Utils.asSection(value)?.let { section ->
                            {
                                val period = section.getLong(Utils.getSectionKey(section, Property.PERIOD), -1)
                                if (period > 0) {
                                    val reactions = ReactionSerializer.serializeReactions(section[Utils.getSectionKey(section, Property.TASKS)])
                                    if (reactions.isNotEmpty()) map[period] = reactions
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
                val func = c.getFunctions()
                if (func != null) {
                    Utils.asSection(func)?.getValues(true)?.entries?.forEach {
                        funs.add(InternalFunction(it.key, it.value.toString()))
                    }
                }
                return@let funs
            }
        )

        return MenuSettings(titles, options, bindings, events, tasks, funs)
    }

    private fun loadLayout(c: MenuConfiguration): MenuLayout {

    }

    private fun loadIcons(c: MenuConfiguration): Set<Icon> {
    }

}