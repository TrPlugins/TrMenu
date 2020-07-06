package me.arasple.mc.trmenu.modules.action.reader

import me.arasple.mc.trmenu.modules.action.base.Option

/**
 * @author Arasple
 * @date 2020/2/27 9:48
 */
object OptionReader {

	fun readOption(string: String): Result {
		val result = Result()
		var content = string
		Option.values().forEach {
			val matcher = it.matcher(content)
			if (matcher.find()) {
				val group = matcher.group()
				val args = group.removeSuffix(">").split(Regex("[:=~]"), 2)
				result.options[it] = if (args.size >= 2) args[1] else "null"
				content = content.replace(group, "")
			}
		}

		result.content = content.removePrefix(" ")
		return result
	}

	class Result(var content: String, val options: MutableMap<Option, String>) {
		constructor() : this("", mutableMapOf<Option, String>())
	}

}