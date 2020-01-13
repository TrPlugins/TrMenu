# TrMenu Update Logs #

#### VERSION 1.1
  - ##### 1.11
    - Since: 2020.1.12
    - Updates:
      - 修复了菜单即使不满足打开条件也会执行打开动作的问题
      - 彻底修复动态效果慢的问题
      - 优化了 ChestCommands 导入
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