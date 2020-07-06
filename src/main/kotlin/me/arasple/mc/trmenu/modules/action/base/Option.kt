package me.arasple.mc.trmenu.modules.action.base

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/2/26 12:48
 */
enum class Option(regex: String) {

	/**
	 * 概率参数, 简写 <c:0.5>
	 */
	CHANCE("<(?i)(c|chance|rate):( )?([0-9]+[.]?[0-9]*>)"),

	/**
	 *延时参数，简写 <d:20>
	 */
	DELAY("<(?i)(d|delay|wait):( )?([0-9]+[.]?[0-9]*>)"),

	/**
	 *玩家集合执行, 简写 <p>
	 */
	PLAYERS("<(?i)((p|(for|all)?(-)?players))(:)?(.+)?>"),

	/**
	 *执行该动作需满足的条件, 简写 <r:___>
	 */
	REQUIREMENT("<(?i)(r|require(ment)?|condition):( )?(.+>)");

	private var pattern: Array<Pattern> = arrayOf(Pattern.compile("$regex>"), Pattern.compile(regex))

	fun matcher(content: String): Matcher {
		val matcher = pattern[0].matcher(content)
		return if (matcher.find()) {
			pattern[1].matcher(matcher.group())
		} else {
			pattern[1].matcher(content)
		}
	}

}