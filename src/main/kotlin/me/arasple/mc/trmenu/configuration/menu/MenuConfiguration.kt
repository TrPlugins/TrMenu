package me.arasple.mc.trmenu.configuration.menu

import me.arasple.mc.trmenu.configuration.BaseConfiguration
import me.arasple.mc.trmenu.configuration.property.Property

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


}