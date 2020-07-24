package me.arasple.mc.trmenu.configuration

import me.arasple.mc.trmenu.configuration.property.Property
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration

/**
 * @author Arasple
 * @date 2020/6/27 20:29
 */
abstract class BaseConfiguration : YamlConfiguration() {

    protected fun getValue(property: Property) = get(getPropertyKey(property))

    protected fun getValue(section: ConfigurationSection?, property: Property) = section?.get(getPropertyKey(section, property))

    protected fun setValue(property: Property, value: Any?) = set(getPropertyKey(property), value)

    protected fun setValue(section: ConfigurationSection?, property: Property, value: Any) = section?.set(getPropertyKey(section, property), value)

    protected fun getPropertyKey(property: Property) = getPropertyKey(this, property)

    protected fun getPropertyKey(section: ConfigurationSection?, property: Property) = Utils.getSectionKey(section, property)

    protected fun getSection(property: Property) = getConfigurationSection(getPropertyKey(property))

}