package me.arasple.mc.trmenu.module.conf.prop

import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration

/**
 * @author Arasple
 * @date 2020/6/27 21:18
 */
@Suppress("UNCHECKED_CAST")
enum class Property(val default: String, val regex: Regex) {

    /**
     * 菜单标题
     */
    TITLE("Title", "(title|name)s?"),

    /**
     * 动态标题更新周期
     */
    TITLE_UPDATE("Title-Update", "titles?-?updates?"),

    /**
     * 菜单布局
     */
    LAYOUT("Layout", "(layout|shape)s?"),

    /**
     * 菜单布局 - 玩家容器
     */
    LAYOUT_PLAYER_INVENTORY("PlayerInventory", "(layout|shape)?-?player-?inv(entory)?s?"),

    /**
     * 容器类型
     */
    INVENTORY_TYPE("Type", "(inv(entory)?)?-?types?"),

    /**
     * 容器大小
     */
    SIZE("Size", "(size|row)s?"),

    /**
     * 菜单选项设置
     */
    OPTIONS("Options", "(option|setting)s?"),

    /**
     * 菜单选项 - 是否启用传递参数
     */
    OPTION_ENABLE_ARGUMENTS("Arguments", "(transfer)?-?arg(ument)?s?"),

    /**
     * 菜单选项 - 默认参数
     */
    OPTION_DEFAULT_ARGUMENTS("Default-Arguments", "def(ault)?-?arg(ument)?s?"),

    /**
     * 菜单选项 - 解锁槽位
     */
    OPTION_FREE_SLOTS("Free-Slots", "free-?slots?"),

    /**
     * 菜单选项 - 默认布局
     */
    OPTION_DEFAULT_LAYOUT("Default-Layout", "def(ault)?-?lay(out)?s?"),

    /**
     * 菜单选项 - 隐藏玩家容器
     */
    OPTION_HIDE_PLAYER_INVENTORY("Hide-Player-Inventory", "hide-?player-?inv(entory)?s?"),

    /**
     * 菜单选项 - 防频繁点击
     */
    OPTION_MIN_CLICK_DELAY("Min-Click-Delay", "min-?click-?delay"),

    /**
     * 菜单选项 - 依赖的 PlaceholderAPI 拓展
     */
    OPTION_DEPEND_EXPANSIONS("Depend-Expansions", "depend-?expansions?"),

    /**
     * 菜单绑定
     */
    BINDINGS("Bindings", "(bind(ing)?|bound)s?"),

    /**
     * 菜单绑定 - 命令
     */
    BINDING_COMMANDS("Commands", "commands?"),

    /**
     * 菜单绑定 - 物品
     */
    BINDING_ITEMS("Items", "items?"),

    /**
     * 菜单事件
     */
    EVENTS("Events", "events?"),

    /**
     * 菜单事件 - 开启菜单
     */
    EVENT_OPEN("Open", "opens?"),

    /**
     * 菜单事件 - 关闭菜单
     */
    EVENT_CLOSE("Close", "closes?"),

    /**
     * 菜单事件 - 点击菜单
     */
    EVENT_CLICK("Click", "clicks?"),

    /**
     * 需求条件
     */
    REQUIREMENT("condition", "(require(ment)?|condition)s?"),

    /**
     * 优先级
     */
    PRIORITY("priority", "pri(ority)?s?"),

    /**
     * 继承（默认图标）
     */
    INHERIT("inherit", "inherits?"),

    /**
     * 周期
     */
    PERIOD("period", "(period|time)s?"),

    /**
     * 执行动作集合
     */
    ACTIONS("actions", "(list|action|click|execute|cmd)s?"),

    /**
     * (拒绝) 反馈动作
     */
    DENY_ACTIONS("deny-actions", "deny(-)?(list|action|click|execute|cmd)?s?"),

    /**
     * 菜单图标
     */
    ICONS("Icons", "(icon|button)s?"),

    /**
     * 菜单图标 - 刷新
     */
    ICON_REFRESH("refresh", "refreshs?"),

    /**
     * 菜单图标 - 更新
     */
    ICON_UPDATE("update", "updates?"),

    /**
     * 菜单图标 - 显示
     */
    ICON_DISPLAY("display", "displays?"),

    /**
     * 菜单图标显示 - 页码
     */
    ICON_DISPLAY_PAGE("page", "pages?"),

    /**
     * 菜单图标显示 - 槽位
     */
    ICON_DISPLAY_SLOT("slot", "(slot|pos(ition)?)s?"),

    /**
     * 菜单图标显示 - 物品名称
     */
    ICON_DISPLAY_NAME("name", "(display)?-?names?"),

    /**
     * 菜单图标显示 - 物品材质
     */
    ICON_DISPLAY_MATERIAL("material", "(mat(erial)?|texture)s?"),

    /**
     * 菜单图标显示 - 物品描述
     */
    ICON_DISPLAY_LORE("lore", "(display)?-?lores?"),

    /**
     * 菜单图标显示 - 物品数量
     */
    ICON_DISPLAY_AMOUNT("amount", "(amt|amount)s?"),

    /**
     * 菜单图标显示 - 发光
     */
    ICON_DISPLAY_SHINY("shiny", "(shiny|glow)s?"),

    /**
     * 菜单图标显示 - 标签
     */
    ICON_DISPLAY_FLAGS("flags", "flags?"),

    /**
     * 菜单图标显示 - NBT
     */
    ICON_DISPLAY_NBT("nbt", "nbts?"),

    /**
     * 菜单图标 - 子图标
     */
    ICON_SUB_ICONS("icons", "(sub|priority)?icons?"),

    /**
     * 菜单内置任务
     */
    TASKS("Tasks", "(task|schedule)s?"),

    /**
     * 菜单内置脚本
     */
    FUNCTIONS("Functions", "(fun(ction)?|script)s?");

    constructor(default: String, regex: String) : this(default, Regex(regex))

    override fun toString(): String = default

    fun <T> of(conf: Map<String, Any>, def: T, nestedList: Boolean = false): T {
        return of(conf, regex, def, nestedList)
    }

    fun getKey(conf: Map<String, Any>): String {
        return getKey(conf, regex) ?: default
    }

    companion object {

        /**
         * PRIVATE CONSTS
         */

        val MAP = mapOf<String, Any>()
        val LIST = listOf<String>()
        val LIST_ANY = listOf<Any>()
        val LISTS = listOf<List<String>>()

        fun asStringList(any: Any): List<String> {
            return asList(any) as List<String>
        }

        fun getKey(conf: Map<String, Any>, regex: Regex): String? {
            return conf.keys.find { it.toLowerCase().matches(regex) }
        }

        fun <T> of(conf: Map<String, Any>, regex: Regex, def: T, nestedList: Boolean = false): T {
            return getKey(conf, regex).let {
                when (def) {
                    is List<*> -> {
                        if (nestedList || def.firstOrNull() is List<*>) asLists(conf[it]) as T
                        else asList(conf[it]) as T
                    }
                    else -> conf[it] as T
                }
            } ?: def
        }

        fun <T> asList(any: T): List<T> {
            if (any == null) return mutableListOf()
            val result = mutableListOf<T>()
            when (any) {
                is List<*> -> any.forEach { result.add(it as T) }
                else -> result.add(any as T)
            }
            return result
        }

        fun <T> asLists(any: T): List<List<T>> {
            val results = mutableListOf<List<T>>()
            any ?: return results
            when (any) {
                is List<*> -> {
                    if (any.isNotEmpty()) {
                        if (any[0] is List<*>) any.forEach { results.add(it as List<T>) }
                        else results.add(any as List<T>)
                    }
                }
                else -> results.add(listOf(any))

            }
            return results
        }

        fun toMap(yaml: MemorySection): Map<String, Any> {
            val map = mutableMapOf<String, Any>()
            yaml.getValues(false).forEach { (key, value) ->
                when (value) {
                    is MemorySection -> map[key] = toMap(value)
                    else -> map[key] = value
                }
            }
            return map
        }

        fun castMap(it: Any): Map<String, Any> {
            return it as Map<String, Any>
        }

        fun asSection(any: Any?): MemorySection? = YamlConfiguration().run {
            when (any) {
                is MemorySection -> return any
                is Map<*, *> -> {
                    any.entries.forEach { entry -> set(entry.key.toString(), entry.value) }
                    return@run this
                }
                is List<*> -> any.forEach { any ->
                    val args = any.toString().split(Regex(":"), 2)
                    if (args.size == 2) set(args[0], args[1])
                    return@run this
                }
            }
            return@run null
        }

    }

}