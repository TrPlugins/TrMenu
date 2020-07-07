package me.arasple.mc.trmenu.modules.configuration

import me.arasple.mc.trmenu.modules.configuration.property.Property
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.util.regex.Pattern

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

    /**
     * Comment Yaml
     *
     * @author MiaoWoo
     * @from https://www.mcbbs.net/thread-1016632-1-3.html
     *
     */

    override fun loadFromString(contents: String) {
        val lastComments = mutableListOf<String>()
        val builder = buildString {
            var id = 0
            contents.split("\n").forEach {
                var matcher = fromPattern.matcher(it)
                if (matcher.find()) {
                    val originComment = matcher.group(2)
                    val splitComments = split(originComment, commentSplitWidth)
                    for (i in splitComments.indices) {
                        var comment = splitComments[i]
                        if (i == 0) comment = comment!!.substring(1)
                        comment = COMMENT_PREFIX + comment
                        lastComments.add(escape(comment))
                    }
                } else {
                    matcher = countSpacePattern.matcher(it)
                    if (matcher.find() && lastComments.isNotEmpty()) {
                        for (comment in lastComments) {
                            append(matcher.group(1))
                            append(matcher.group(2) ?: "")
                            append(commentPrefixSymbol)
                            append("$comment[$id]")
                            append(commentSuffixSymbol)
                            append("\n")
                            id++
                        }
                        lastComments.clear()
                    }
                    append(it)
                    append("\n")
                }
            }
        }
        super.loadFromString(builder)
    }

    override fun saveToString() = buildString {
        super.saveToString().split("\n").forEach {
            var part = it
            val matcher = toPattern.matcher(part)
            if (matcher.find() && matcher.groupCount() == 5) {
                part = (matcher.group(1) ?: "") + matcher.group(4).substringBeforeLast("[")
            }
            append(recover(part))
            append("\n")
        }
    }

    companion object {

        private var commentPrefixSymbol = "'COMMENT "
        private var commentSuffixSymbol = "': COMMENT"
        private var fromRegex = "( *)(#.*)(?<!([':]))\$"
        private var fromPattern = Pattern.compile(fromRegex)
        private var toRegex = "( *)(- )*($commentPrefixSymbol)(#.*)($commentSuffixSymbol)"
        private var toPattern = Pattern.compile(toRegex)
        private var countSpacePattern = Pattern.compile("( *)(- )*(.*)")
        private var commentSplitWidth = 90

        private fun recover(string: String) = string.replace("．", ".").replace("＇", "'").replace("：", ":")

        private fun escape(string: String) = string.replace(".", "．").replace("'", "＇").replace(":", "：")

        private fun split(string: String, partLength: Int): Array<String?> {
            val array = arrayOfNulls<String>(string.length / partLength + 1)
            for (i in array.indices) {
                val beginIndex = i * partLength
                var endIndex = beginIndex + partLength
                if (endIndex > string.length) endIndex = string.length
                array[i] = string.substring(beginIndex, endIndex)
            }
            return array
        }

    }

}