# TrMenu Update Logs #

#### VERSION 1.1
  - ##### 1.18
    - Since: 2020.2.7
    - Updates:
      - **[*NEW*]** TabooLib: 支持 OpenJ9, Hotspot !!!
      - 新增自定义物品动态 NBT 标签，用法
        ```YAML
        display:
          name: 'Example'
          amount: 3
          # <key>:<value>
          # 轻松添加自定义 NBT 标签，无需使用 Json 物品
          # 可以使用变量
          nbt:
            Unbreakable: 1
        ```
      - 新增列出菜单的过滤参数 /trmenu list <filter>
      - 新增音效动作中使用变量
      - 新增停止播放玩家音效的方法 `sound: stop`
      - 新增选项 `OPTIONS.ANTI-ITEM-FLICKERING`
      - 新增支持微损导入 TabooMenu 菜单
      - 新增中文版 TrMenu About GUI
      - 支持 MENU-FILES 指定路径加载包含多个菜单的文件夹
      - 修复了 `REFRESH` 动作无法更新图标显示
      - 修复了 `ALL` 类型的动作反复堆叠到其它类型中
      - 修复了偶尔出现的空按钮槽的问题
  - ##### 1.17
    - Since: 2020.2.7
    - Updates:
      - 你现在可以在快捷打开菜单方式里面设置为执行动作
      - 你现在在 DeluxeMenus 的迁移中可以选择性使用布局功能
      - 修复了 DeluxeMenus 的 Custom Model Data 无法迁入
      - 修复了快捷键F和部分插件的不兼容
      - 新增动作 `- 'set-title: CONTENT''` 二次设置容器标题，支持变量
      - 新增命令 `/trmenu sounds <filter>` 轻松预览音效
      - 新增 `parseMat` 方法到 `me.arasple.mc.trmenu.api.TrMenuAPI`
      - 为命令 `/trmenu runAction` 增加了更多详细的反馈信息, 方便诊断动作
      - 将 [快速开启] 功能更改为可选配置项 `settings.yml` `OPTIONS.FAST-OPEN`, 默认关闭 [#24](https://github.com/Arasple/TrMenu/issues/24)
  - ##### 1.16
    - Since: 2020.2.2
    - Updates:
      - 修复了菜单文件监听器有时不工作
      - 修复了无法通过 `close-actions` 打开上级菜单
      - 极大的提升了打开菜单的速度
      - 新增在 Json 物品中使用标签 `<variable>` 以启用相关变量的支持
      - 新增对 JDK11 的支持 (暂时不支持 JDK9 或 OpenJ9)
      - 为各级子命令均添加了独立的权限，例如 `trmenu.command.open` 对 `/trmenu open`
      - 新增命令 `/trmenu runAction <Player> <ActionLine>` 测试动作执行效果
      - 新增 Tellraw 动作 (发送Json消息)，非常轻松的构建 Json 组件
      - > 你可以发送原始 Json 消息格式，也可以使用 TrMenu 简单的构建方式
      - > 例如
      - >```YAML
        >- 'json: &3Hello, &a%player_name%&3! Click <&a&nHERE?hover=&7Click to open website?url=https://trmenu.trixey.cn> &3to see our wiki.
        >```
  - ##### 1.15
    - Since: 2020.1.27
    - Updates:
      - 修复了 Double 值无法被应用为动态物品数量的 bug
      - 修复了材质标签 `<variable>` 无效
      - 新增 `KEEP-OPEN-REQUIREMENT` 选项. 将会周期性检测条件是否满足, 若不满足则强制关闭菜单, 示例: `"%vault_eco_balance%" > 1000`, `%vault_rank% == "VIP";20` <--- 定义周期用 ; 分开 20ticks
      - 新增 `getItemBuildr()` 在 TrUtils 中
      - 新增多布局 (Shapes) 支持，创建多页菜单从未如此简单 [#20](https://github.com/Arasple/TrMenu/issues/20)
      - 新增一个动作设置当前页面 (Shape), 示例 `set-shape: 0`, 或 `shape: 1`, 提供变量 `{page}` 显示为当前布局位置+1, 即实际页面
      - 新增 bStats 统计自动更新器的启用情况
      - 支持自动补位图标, 如果你设置图标槽位为负数，将动态更新位置到第一个空位处
      - 现在你可以调用方法，为 TrMenu 的 JS 处理添加自定义对象
        `JavaScript.getBindings().put(String key, Object object)`
  - ##### 1.14
    - Since: 2020.1.24
    - Updates:
      - 修复了 Title 动作的空指针异常 (当不设置副标题时)
      - 修复了传入菜单的参数在 `Open-Requirement`/`Open-Deny-Actions` 中不可用
      - 简化了更新检测的通知消息
      - 新增了配置文件自动更新功能
      - 新增了插件自动更新功能, 默认禁用, 需手动开启 (关服时若已发现新版本, 则自动下载)
      - 新增命令 `/trmenu update` 轻松的手动更新 TrMenu
      - 新增 Js-Item，可用在物品材质中，返回 ItemStack 对象, 例如
        `<js-item:player.getInventory().getArmorContents()[3]>`
      - 新增右键玩家绑定和蹲下右键玩家绑定，可轻松创建高级玩家交互 GUI
      - 新增了默认示例菜单`profile.yml`, 为上方功能提供参考
      - 支持 Oraxen 插件物品直接调用, 用法 `'<oraxen:ID>'`
  - ##### 1.13
    - Since: 2020.1.20
    - Updates:
      - 新增语言文件 vi_VN , 由 Galaxyvietnam 翻译
      - 新增语言文件 th_TH , 由 DriteStudio 翻译
      - 新增动作 ForceClose 在不跑 close actions 的情况下关闭菜单
      - 新增选项 option.silent 关闭菜单启动时的 Logo
      - 新增在线模板功能, 快速生成菜单
      - 新增命令 /trmenu item 将物品转换为 JSON 支持NBT，可以在 Mats 中使用
      - 现已支持无敌动态标题!!!
      - 修复了一个优先级图标刷新问题
      - R1
        - DeluxeMenus 100%无损暴打现已支持
      - R2
        - 修复了 1.14 以下版本容器动态标题的问题
        - 现在支持迁移 DeluxeMenus 的 config.yml 内部菜单
        - 优化了容器动态标题实现代码
      - R3
        - Update depend TabooLib to 5.15
        - 新增语言文件 Spanish , 由 brs73 翻译
        - 'Shape' 不再强制需要，你可以只写 slot
        - 新增 `TrUtils.parseBracketPlaceholders(Player player, String string)` 方法，可在Catcher动作里转 {} PAPI变量 
        - 新增 `OPTIONS.CATCHER-CANCEL-WORDS` 选项在 `settings.yml` 自定义取消动作的标题
        - 你现在可以定义 `ROWS` 或 `SIZE` 调整箱子容器的行数，如果你不用 `Shape`
        - 给/扣钱 动作现在支持使用变量
        - 新增 bStats 统计项 Menu-Item types, inventory types
        - 修复了 NBT 物品转 Json 问题
        - 修复了在线模板的一个问题 (当你一次性放很多很多不同的物品)
        - 修复了分享命令 (Share) 的 NullPointerException 异常
        - 修复了 Hastebin 粘贴的编码问题 (之前不支持特殊符号/中文)
        - 新增命令参数 (`/trmenu item <json>`) 实现 Json 回转物品
        - 为 1.9+ 服务器新增选项快捷绑定菜单到 副手(或蹲下+副手) 即 F / Shift+F
        ```YAML
        # Only for 1.9+ servers
        # Each option is only for one menu
        SHORTCUT-OPEN:
          OFFHAND: null
          SNEAKING-OFFHAND: 'example'
        ```
  - ##### 1.12
    - Since: 2020.1.14
    - Updates:
      - 优化 Command(Player/OP/Console) 执行方式
      - 新增动作 ActionGiveMoney - 给予玩家余额
      - 新增动作 ActionTakeMoney - 扣除玩家余额
      - 新增动作 ActionTakeItem - 扣除多个指定条件的物品
      - 现在支持在 settings.yml-MENUS 节点下写 List 创建菜单
      - 修复了更新检测版本号通知错误
      - 新增 zh_TW 繁体中文语言文件
      - R2
        - 新增 MenuOptions.Update-Inventory 用以实现流畅的动画,
          防止手中物品闪频，插件讲自动切换到空手位 (关菜单后自动复原)
        - 修复了多个玩家同时打开菜单时物品动画更新问题
          现已更改为每个玩家独立的频率，且关闭时重置
        - 新增 TrUtils，可在 JavaScript 中使用，轻松制作高级菜单
        - 重写 Mat 相关内容
        - 新增 <Dye: r,g,b> 选项，写Mat里面支持染色皮革
        - 新增 <Banner: PATTERN> 选项，绘自定义旗帜
      - R3
        - 新增 ChatCatcher（聊天捕获器），例如
          ```
          - |-
            Catcher:
              <Type=CHAT>
              <Before=Tell: &3&lPlease type a value>
              <Valid=TELL:&6You typed a number &a$input>
              <Invalid=TELL:&cInvalid number input>
              <Require=TrUtils.isNumber("$input")>
              <Cancel=TELL:&7Canceld...>
          ```
      - R4
        - 彻底修复了菜单文件监听失效
        - 新增自动重载更新菜单时错误的诊断提示
        - 修复了动作冲突中断执行的问题
        - 修复了优先级读取顺序, 多个优先级图标时请使用 priority 项校正
        - 修复了一个 bStats 报错
        - 优化了 1.13 以下版本的物品ID读取方式
        - 兼容支持了 Forge 端中 Mod 物品的 ID 使用
      - R5
        - 现支持用 .json 写菜单
        - 修复了动态位置的物品在多个玩家同时打开时的冲突
        - 新增 $openBy 变量，可在 openRequirement 中使用, 返回 CONSOLE / PLAYER
      - R6
        - TrUtils 新增 getPlayerArgs 方法
        - 修复了数字键快捷位移背包物品到菜单的bug
        - 新增了木牌捕获器类型
        - 修复了聊天捕获器属性读取错误
        - 新增了 Refresh 动作，刷新所有优先级图标
        - 新增 SetArgs 动作，菜单内设置参数
      - R7
        - 新增了 SetSlots 动作，二次更改图标位置
        - 修复了 JavaScript 中 event, clickItemStack 等对象无法使用的问题
        - 优化了图标动态位置的结构
        - 新增了 ClearSlots 动作，快速清除残留位置 (每次更新都会自动清理)
        - 兼容了 发射器 和漏洞 容器的 Shape 排版
        - 新增调试信息: 显示点击容器的位置
  - ##### 1.11
    - Since: 2
    020.1.12
    - Updates:
      - 修复了菜单即使不满足打开条件也会执行打开动作的问题
      - 彻底修复动态效果慢的问题
      - 优化了 ChestCommands 导入
      - R1
        - 修复了优先级图标刷新问题
      - R2
        - 优化了 en_US 语言
        - 修复了关闭菜单动作在高版本报错的问题
      - R4
        - 修复了部分无参动作读取需要冒号的问题
        - 新增了 /TrMenu About 关于插件的菜单
        - 修复了 Command 相关命令执行报错的问题
        - 新增了 %Trmenu_Query_url|query% 无敌变量
        - 现在仅会在刷新频率低于 10 时调用 player.updateInventory(), 防止动画跟不上
      - R5
        - 改进 "about" GUI
        - 新增 TrMenu PlaceholderAPI 变量
  - ##### 1.1
    - Date: 2019.12.31
    - Overview:
      - 元旦快乐 :)
      - 重构大量代码
      - 重构动作模块 (TrAction)
      - 优化体验
      - [!] 请+群及时反馈相关问题 
    - Updates:
      - 改用 Gradle
      - 更新最低依赖至 5.13
      - 修复 PlaceholderAPI 自动下载链接失效问题
      - 删改一些动作，新写法请等待文档更新
      - 完善动作标签使用体验
      - 支持单行多个动作、共享参数等等
      - 新增对 1.15 的兼容支持
      - 改进插件载入方式
      - 重写材质读取方面
      - 删除对 Js/Icon Refresh 次数的统计
      - 新增一个 物品的 “出售、收购” 示例菜单
      - 删除各类 "公告" 内动作，改用 <players:> 标签
      - 支持用 \_||\_ 符号隔离开多个动作
      - R2:
        - 新 增了 en_US 语言
        - 改善了默认示例菜单
        - 修复了 CatServer 材质读取问题
        - 修复了点击动作中 JavaScript 无法用 ClickEvent 相关对象的问题
        - 跨服动作现在支持使用变量
      - R3:
        - 新增自动检测并转换导入 ChestCommands 的菜单 (仅支持迁入基础样式、布局模板)

#### Old logs
	Version 1.0:
		Date: 2019.8.16
		Updates:
		 - 正式版
	Version 1.0X:
      Date: 2019.10.5 - Now
      Updates:
        1.07:
          - 更新自定义菜单的容器类型, 支持 23+ 种 InventoryType
          - · CHEST / DISPENSER / DROPPER / FURNACE / WORKBENCH / CRAFTING ......
          - 现在替换PlaceholderAPI得到的布尔值无需用JS进行判断
          - <REQUIREMENT:"%checkitem_mat:DIAMOND%" == "true"> --> <REQUIREMENT:%checkitem_mat:DIAMOND%>
          - PlaceholderAPI 改为必需前置
          - 新增当版本过旧时醒目的更新提醒
          - 修复了表达式匹配导致的变量判断符号报错
          - 修复了 CheckItem 变量无法使用
          - 修复了菜单绑定物品必须要点物品才能打开的问题
          - 修复了多个动作选项同时用时的错误
          - 新增 bStats 统计 inventory_type
        1.06:
          - 修复了关闭条件导致JS循环报错
          - 修复了动态位置异常
          - 修复了 Title 公告无效
          - 修复了不能部分属性未赋值的Title发送显示“null”
          - 修复了未设置物品名称时不能正确显示原版名称
          - 修复了打开菜单命令一个空指针情况
          - 修复了命令参数显示在消息命令中
          - 新增"公告类"动作的权限参数, 只公告给有权限的玩家
          - 新增了插件信息显示
          - 新增对 PAPI/HDB 挂钩情况的统计
          - 更新和优化大量文档内容
          - 新增对 YAML 格式错误的提示
          - 新增无敌自动监听重载菜单，动态编辑实时生效
          - 现在条件图标未定义的，将会继承默认图标已定义的显示属性
          - 重写了一个更牛逼的默认菜单
        1.05:
          - 重写菜单载入代码
          - 单个动作支持参数 <requirement> | <chance> | <delay> 条件/概率/延时
          - 修复了 Lore 无法刷新变量的问题
          - 修复了动作执行一次后失效
          - 修复了头颅异步读取报错
          - 修复了绑定物品监听报错问题
          - 修复了动态位置残留问题
          - 新增错误语言配置
          - 现在允许使用空气材质
        1.04:
          - 修复了更新周期异常
          - 代码优化改进
          - 新增菜单的 Close-Requirement/Close-DenyActions (关闭条件表达式) 和 (关闭失败执行动作)
          - 按钮的条件图标现在现在若未配置 display/actions 将采用默认图标的代替
          - 改进了 "物品材质" 模块读取, 支持忽略大小写、多种节点写法
          - 菜单新增一个 Options.Depend-Expansions 设置菜单依赖的变量拓展, 自动下载
          - 修复了 未指定开启命令/绑定物品 的菜单报错问题
          - 重载菜单时强制关闭正在查看的菜单并予以通知
        1.03:
          - 改进代码, 增加了注释
          - 异步缓存并更新玩家头颅, 零卡顿
          - 自定义节点物品现在忽略大小写
          - 修复了 bStats 的bug
          - 新增 Head Database 调用支持, <HDB-[id]>(指定), <HDB-Random>(随机)
          - 单个 Icon 支持异步刷新多组动态位置!!
          - 新增菜单的 Open-Requirement (打开条件表达式) 和 Open-DenyActions (打开失败执行动作)
          - 新增聊天捕获器图标命令
        1.02:
          - 新增自定义 ItemFlags
          - 新增附魔发光效果 (支持动态表达式!)
          - 新增动态数量定义，支持表达式
          - 新增 bStats 统计 Js 运算次数、菜单行数、图标刷新次数
          - 现在重载时将关闭在线玩家打开的菜单
        1.01:
          - Js 处理新增 bukkit.getServer(), itemStack 对象
          - 新增 Break Action，中止执行动作
          - 新增发送自定义 Title 动作
          - 新增发送自定义 ActionBar 动作
          - 新增公告全体玩家 Title 动作
          - 新增公告全体玩家 ActionBar 动作
          - 文档完善
          - 完善 debug 命令
    
    Version 1.0:
      Date: 2019.10.5
      Updates:
        - 正式版!
        - 代码改进
        - 修复了一吨BUG
        - 现在可在低于1.13版本上用1.13+的材质写法
        - 新增 Js 动作
        - 新增 延时动作
        - 绑定菜单到带特殊Lore的物品上
        - 修复玩家头颅无法正确获取
        - 每个动作可配置独立的条件需求、执行概率
    
    Version 0.2-α:
      - 手滑发出去了