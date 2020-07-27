package me.arasple.mc.trmenu.configuration.property

/**
 * @author Arasple
 * @date 2020/6/27 21:18
 */
enum class Property(val default: String, val regex: Regex) {

    /**
     * 菜单标题
     */
    TITLE("Title", "title(s)?"),

    /**
     * 动态标题更新周期
     */
    TITLE_UPDATE("Title-Update", "title(s)?(-)?update(s)?"),

    /**
     * 菜单布局
     */
    LAYOUT("Layout", "(layout|shape)(s)?"),

    /**
     * 菜单布局 - 玩家容器
     */
    LAYOUT_PLAYER_INVENTORY("PlayerInventory", "(layout|shape)(-)?player(-)?inv(entory)?(s)?"),

    /**
     * 容器类型
     */
    INVENTORY_TYPE("Type", "(inv(entory)?)?(-)?type(s)?"),

    /**
     * 菜单选项设置
     */
    OPTIONS("Options", "(option|setting)(s)?"),

    /**
     * 菜单选项 - 默认参数
     */
    OPTION_DEFAULT_ARGUMENTS("Default-Arguments", "def(ault)?(-)?arg(ument)?(s)?"),

    /**
     * 菜单选项 - 默认布局
     */
    OPTION_DEFAULT_LAYOUT("Default-Layout", "def(ault)?(-)?lay(out)?(s)?"),

    /**
     * 菜单选项 - 隐藏玩家容器
     */
    OPTION_HIDE_PLAYER_INVENTORY("Hide-Player-Inventory", "hide(-)?player(-)?inv(entory)?(s)?"),

    /**
     * 菜单选项 - 防频繁点击
     */
    OPTION_MIN_CLICK_DELAY("Min-Click-Delay", "min(-)?click(-)?delay"),

    /**
     * 菜单选项 - 依赖的 PlaceholderAPI 拓展
     */
    OPTION_DEPEND_EXPANSIONS("Depend-Expansions", "depend(-)?expansion(s)?"),

    /**
     * 菜单绑定
     */
    BINDINGS("Bindings", "(bind(ing)?|bound)(s)?"),

    /**
     * 菜单绑定 - 命令
     */
    BINDING_COMMANDS("Commands", "command(s)?"),

    /**
     * 菜单绑定 - 物品
     */
    BINDING_ITEMS("Items", "item(s)?"),

    /**
     * 菜单事件
     */
    EVENTS("Events", "event(s)?"),

    /**
     * 菜单事件 - 开启菜单
     */
    EVENT_OPEN("Open", "open(s)?"),

    /**
     * 菜单事件 - 关闭菜单
     */
    EVENT_CLOSE("Close", "close(s)?"),

    /**
     * 菜单事件 - 点击菜单
     */
    EVENT_CLICK("Click", "click(s)?"),

    /**
     * 需求条件
     */
    REQUIREMENT("condition", "(require(ment)?|condition)(s)?"),

    /**
     * 优先级
     */
    PRIORITY("priority", "pri(ority)?(s)?"),

    /**
     * 继承（默认图标）
     */
    INHERIT("inherit", "inherit(s)?"),

    /**
     * 周期
     */
    PERIOD("period", "(period|time)(s)?"),

    /**
     * 执行动作集合
     */
    ACTIONS("actions", "(list|action|click|execute|cmd)(s)?"),

    /**
     * (拒绝) 反馈动作
     */
    DENY_ACTIONS("deny-actions", "deny(-)?(list|action|click|execute|cmd)?(s)?"),

    /**
     * 菜单图标
     */
    ICONS("Icons", "(icon|button)(s)?"),

    /**
     * 菜单图标 - 刷新
     */
    ICON_REFRESH("refresh", "refresh(s)?"),

    /**
     * 菜单图标 - 更新
     */
    ICON_UPDATE("update", "update(s)?"),

    /**
     * 菜单图标 - 显示
     */
    ICON_DISPLAY("display", "display(s)?"),

    /**
     * 菜单图标显示 - 页码
     */
    ICON_DISPLAY_PAGE("page", "page(s)?"),

    /**
     * 菜单图标显示 - 槽位
     */
    ICON_DISPLAY_SLOT("slot", "(slot|pos(ition)?)(s)?"),

    /**
     * 菜单图标显示 - 物品名称
     */
    ICON_DISPLAY_NAME("name", "(display)?(-)?name(s)?"),

    /**
     * 菜单图标显示 - 物品材质
     */
    ICON_DISPLAY_MATERIAL("material", "(mat(erial)?|texture)(s)?"),

    /**
     * 菜单图标显示 - 物品描述
     */
    ICON_DISPLAY_LORE("lore", "(display)?(-)?lore(s)?"),

    /**
     * 菜单图标显示 - 物品数量
     */
    ICON_DISPLAY_AMOUNT("amount", "(amt|amount)(s)?"),

    /**
     * 菜单图标显示 - 发光
     */
    ICON_DISPLAY_SHINY("shiny", "(shiny|glow)(s)?"),

    /**
     * 菜单图标显示 - 标签
     */
    ICON_DISPLAY_FLAGS("flags", "flag(s)?"),

    /**
     * 菜单图标显示 - NBT
     */
    ICON_DISPLAY_NBT("nbt", "nbt(s)?"),

    /**
     * 菜单图标 - 子图标
     */
    ICON_SUB_ICONS("icons", "(sub|priority)?icon(s)?"),

    /**
     * 菜单内置任务
     */
    TASKS("Tasks", "(task|schedule)(s)?"),

    /**
     * 菜单内置脚本
     */
    FUNCTIONS("Functions", "(fun(ction)?|script)(s)?"),

    /**
     * 输入捕获器动作
     */
    CATCHER_TYPE("type", "type"),

    CATCHER_BEFORE("before", "before"),

    CATCHER_CANCEL("cancel", "cancel"),

    CATCHER_REACTION("reaction", "reaction(s)?");

    constructor(default: String, regex: String) : this(default, Regex("(?i)$regex"))

    override fun toString(): String = default

}