package me.arasple.mc.trmenu.module.display.texture

/**
 * @author Arasple
 * @date 2021/1/24 12:15
 *
 */
enum class TextureMeta(val regex: Regex) {

    DATA_VALUE("(?i)[<{]data-?value[:=](.+?)[>}]"),

    MODEL_DATA("(?i)[<{]model-?data[:=](\\d+?)[>}]"),

    LEATHER_DYE("(?i)[<{]dye[:=](\\d{3},\\d{3},\\d{3})[>}]"),

    BANNER_PATTERN("(?i)[<{]banner[:=](.+?)[>}]");

    constructor(regex: String) : this(regex.toRegex())

}