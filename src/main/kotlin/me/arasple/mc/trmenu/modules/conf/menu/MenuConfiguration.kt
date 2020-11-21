package me.arasple.mc.trmenu.modules.conf.menu

import me.arasple.mc.trmenu.modules.conf.BaseConfiguration
import me.arasple.mc.trmenu.modules.conf.property.Property

/**
 * @author Arasple
 * @date 2020/6/27 20:29
 */
class MenuConfiguration(val loadedPath: String) : BaseConfiguration() {

    fun getTitle() = getValue(Property.TITLE)

    fun getTitleUpdate() = getValue(Property.TITLE_UPDATE)

    fun getLayout() = getValue(Property.LAYOUT)

    fun getLayoutInventory() = getValue(Property.LAYOUT_PLAYER_INVENTORY)

    fun getInventoryType() = getValue(Property.INVENTORY_TYPE)

    fun getSize() = getValue(Property.SIZE)

    fun getOptions() = getSection(Property.OPTIONS)

    fun getOption(property: Property) = getValue(getOptions(), property)

    fun getOptionEnableArguments() = getOption(Property.OPTION_ENABLE_ARGUMENTS)

    fun getOptionDefaultArguments() = getOption(Property.OPTION_DEFAULT_ARGUMENTS)

    fun getOptionDefaultLayout() = getOption(Property.OPTION_DEFAULT_LAYOUT)

    fun getOptionHidePlayerInventory() = getOption(Property.OPTION_HIDE_PLAYER_INVENTORY)

    fun getOptionMinClickDelay() = getOption(Property.OPTION_MIN_CLICK_DELAY)

    fun getOptionDependExpansions() = getOption(Property.OPTION_DEPEND_EXPANSIONS)

    fun getBindings() = getSection(Property.BINDINGS)

    fun getBinding(property: Property) = getValue(getBindings(), property)

    fun getBindingCommands() = getBinding(Property.BINDING_COMMANDS)

    fun getBindingItems() = getBinding(Property.BINDING_ITEMS)

    fun getEvents() = getSection(Property.EVENTS)

    fun getEvent(property: Property) = getValue(getEvents(), property)

    fun getEventOpen() = getEvent(Property.EVENT_OPEN)

    fun getEventClose() = getEvent(Property.EVENT_CLOSE)

    fun getEventClick() = getEvent(Property.EVENT_CLICK)

    fun getTasks() = getValue(Property.TASKS)

    fun getFunctions() = getValue(Property.FUNCTIONS)

    fun getIcons() = getValue(Property.ICONS)

}