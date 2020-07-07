package me.arasple.mc.trmenu.modules.configuration.menu

import me.arasple.mc.trmenu.modules.configuration.BaseConfiguration
import me.arasple.mc.trmenu.modules.configuration.property.Property

/**
 * @author Arasple
 * @date 2020/6/27 20:29
 */
class MenuConfiguration : BaseConfiguration() {

    fun getTitle() = getValue(Property.TITLE)

    fun getTitleUpdate() = getValue(Property.TITLE_UPDATE)

    fun getOptions() = getSection(Property.OPTIONS)

    fun getOption(property: Property) = getValue(getOptions(), property)

    fun getOptionDefaultArguments() = getOption(Property.OPTION_DEFAULT_ARGUMENTS)

    fun getOptionHidePlayerInventory() = getOption(Property.OPTION_HIDE_PLAYER_INVENTORY)

    fun getOptionMinClickDelay() = getOption(Property.OPTION_MIN_CLICK_DELAY)

    fun getOptionDependExpansions() = getOption(Property.OPTION_DEPEND_EXPANSIONS)

    fun getBindings() = getSection(Property.BINDINGS)

    fun getBinding(property: Property) = getValue(getBindings(), property)

    fun getBindingCommands() = getBinding(Property.BINDING_COMMANDS)

    fun getBindingItems() = getBinding(Property.BINDING_ITEMS)

    fun getBindingShortcuts() = getBinding(Property.BINDING_SHORTCUT)

    fun getEvents() = getSection(Property.EVENTS)

    fun getEvent(property: Property) = getValue(getBindings(), property)

    fun getEventOpen() = getEvent(Property.EVENT_OPEN)

    fun getEventClose() = getEvent(Property.EVENT_CLOSE)

    fun getEventClick() = getEvent(Property.EVENT_CLICK)

    fun getTasks() = getEvent(Property.TASKS)

    fun getFunctions() = getEvent(Property.FUNCTIONS)

}