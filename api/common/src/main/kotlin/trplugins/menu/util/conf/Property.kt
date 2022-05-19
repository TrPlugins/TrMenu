package trplugins.menu.util.conf

import taboolib.module.configuration.ConfigSection
import taboolib.module.configuration.Configuration

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
     * 菜单选项 - 使用纯发包菜单
     */
    OPTION_PURE_PACKET("Pure-Packet", "pure-?pack(et)?s?"),

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
    BINDING_COMMANDS("Commands", "(command|cmd)s?"),

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
    CONDITION("condition", "(require(ment)?|cond(ition)?)s?"),

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

    constructor(default: String, regex: String) : this(default, Regex("(?i)$regex"))

    override fun toString(): String = default

    fun ofString(conf: Configuration?, def: String? = null): String {
        return of(conf, def).toString()
    }

    fun ofBoolean(conf: Configuration?, def: Boolean = false): Boolean {
        return ofString(conf, def.toString()).toBoolean()
    }

    fun ofInt(conf: Configuration?, def: Int = -1): Int {
        return ofString(conf).toIntOrNull() ?: def
    }

    fun ofList(conf: Configuration?): List<Any> {
        return asAnyList(of(conf))
    }

    fun ofIntList(conf: Configuration?, def: List<Int> = listOf()): List<Int> {
        return asIntList(of(conf, def))
    }

    fun ofStringList(conf: Configuration?, def: List<String> = listOf()): List<String> {
        return asList(of(conf, def))
    }

    fun ofSection(conf: Configuration?): Configuration? {
        return asSection(of(conf))
    }

    fun ofMap(conf: Configuration?, deep: Boolean = false): Map<String, Any?> {
        return ofSection(conf)?.getValues(deep) ?: mapOf()
    }

    fun ofLists(conf: Configuration?): List<List<String>> {
        val list = ofList(conf)
        return if (list.firstOrNull() is List<*>) {
            list.map { asList(it) }
        } else {
            val strs = asList(list)
            if (strs.isEmpty()) listOf() else listOf(strs)
        }
    }

    fun of(conf: Configuration?, def: Any? = null): Any? {
        return conf?.get(getKey(conf)) ?: def
    }

    fun getKey(conf: Configuration): String {
        return getSectionKey(conf, this)
    }

    companion object {

        fun asList(any: Any?): List<String> {
            if (any == null) return mutableListOf()
            return when (any) {
                is List<*> -> any.map { it.toString() }
                else -> listOf(any.toString())
            }
        }

        fun asAnyList(any: Any?): List<Any> {
            if (any == null) return mutableListOf()
            val result = mutableListOf<Any>()
            when (any) {
                is List<*> -> any.forEach { it?.let { any -> result.add(any) } }
                else -> result.add(any.toString())
            }
            return result
        }

        fun asIntList(any: Any?): List<Int> {
            if (any == null) return mutableListOf()
            val result = mutableListOf<Int>()
            when (any) {
                is List<*> -> any.forEach { result.add(it.toString().toInt()) }
                else -> result.add(any.toString().toInt())
            }
            return result
        }

        fun asArray(any: Any?): Array<String> = asList(any).toTypedArray()

        fun asIntArray(any: Any?): Array<Int> = asIntList(any).toTypedArray()

        fun asBoolean(any: Any?): Boolean = any.toString().toBoolean()

        fun asInt(any: Any?, def: Int = 0): Int = any.toString().toIntOrNull() ?: def

        fun asLong(any: Any?, def: Long = 0): Long = any.toString().toLongOrNull() ?: def

        fun <T> asLists(any: Any): List<List<T>> {
            val results = mutableListOf<List<T>>()
            when (any) {
                is List<*> -> {
                    if (any.isNotEmpty()) {
                        if (any[0] is List<*>) any.forEach { results.add(it as List<T>) }
                        else results.add(any as List<T>)
                    }
                }
                else -> results.add(listOf(any as T))
            }
            return results
        }

        fun asSection(any: Any?): Configuration? = Configuration.empty().let {
            when (any) {
                is Configuration -> return any
                is ConfigSection -> {
                    return Configuration.empty().also { it.root = any.root }
                }
                is Map<*, *> -> {
                    any.entries.forEach { entry -> it[entry.key.toString()] = entry.value }
                    return@let it
                }
                is List<*> -> any.forEach { any ->
                    val args = any.toString().split(Regex(":"), 2)
                    if (args.size == 2) it[args[0]] = args[1]
                    return@let it
                }
            }
            return@let null
        }

        fun getSectionKey(section:  Configuration?, property: Property) =
            getSectionKey(section, property.regex, property.default, false)

        fun getSectionKey(section:  Configuration?, regex: Regex, default: String = "", deep: Boolean = false) =
            section?.getKeys(deep)?.firstOrNull { it.matches(regex) } ?: default

    }

}