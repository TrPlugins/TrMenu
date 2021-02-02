# TrMenu Ⅲ Change Logs

---

### 未完成

- 内置函数 & 外置函数 自定义
- 更多 Kether 动作用以检测
- 模板创建功能
- 性能损耗查看功能
- 测试
  - [ ] 捕获器
- 示例菜单
- 日志

---

### 底层

- 配置文件 & 语言文件 格式全部重新设计
- 90% 代码完全重写, 插件体积减少 20%， 瞄准性能优化
- 发包虚拟容器结构完全重制，预期更快 & 灵活  
  且有望最终解决动态标题频闪问题
  
---

### 菜单

##### 功能

- 新增 `Free-Slot` 项，解除目标槽位的物品锁定

##### 材质

```
head:%player_name%
head:BlackSky

head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRmNDUyZDk5OGVhYmFjNDY0MmM2YjBmZTVhOGY0ZTJlNjczZWRjYWUyYTZkZmQ5ZTZhMmU4NmU3ODZlZGFjMCJ9fX0=
head:44f452d998eabac4642c6b0fe5a8f4e2e673edcae2a6dfd9e6a2e86e786edac0

repo:myCustomItem
repo:%custom_variable_whichreturnstheid%

source:HDB:random
source:ORAXEN:itemId

{json Content}
```

- 特殊物品不再需要 { } 、<> 等
- HeadDatabase / Oraxen 等物品挂钩需要以 ItemSource 格式配置
- SkinsRestorer 的挂钩和自定义材质头颅一律集合到玩家头颅中，将自动检测处理

---

### 条件

条件检测是本次更新最大的改动部分

现在条件弃用了 TrMenu 2 所提供的 “智能表达式”，仅支持 **Kether** 和 **JavaScript** 两种语句

且默认使用 Kether 语句

- **Kether**
  - Kether 官方文档会提供使用说明

- **JavaScript**
  - 需要 js 前缀，例如 `condition: 'js: player.getHealth() > 20.0'`
  - 全部缓存，不支持动态编译变量，使用变量需要套函数
  - 默认提供**对象**
    - `player`
    - `bukkitServer`
    - `utils`
   - 默认提供**函数**
       - `var("STRING")` 编译变量，返回 String
       - `varInt("STRING")` 编译变量，返回整型



> 弃用 v2 写法原因: 
>
>     1. 臃肿不灵活，不完善引发的错误多
>     2. 底层还是 JavaScript 且没有规范缓存、变量处理

---

### 函数

函数是你能够在字符串中自由使用的内容，可视作高级变量

目前支持的函数种类如下

- **Argument** Function: `{<Index>}`
- **Kether** Function: `${ke: function}`
- **JavaScript** Function: `${js: function}`
- **Metadata** Function
  - `${meta: [KEY]}`
  - `${data: [KEY]}`
  - `${globalData/gData: [key]}`
- **Extra** Functions * 待定, 暂未完成
  - `${id}`

> 函数的 `$` 标识符可以不写

字符串替换优先级

- 菜单传参 `{0}`
- 函数替换
- 颜色替换

---

### 动作

- **ActionBound** 弃用 v1 遗留的 _\||\_ 写法，现阶段统一为 `&&&`
- **Title** 动作统一规范格式为
  - `title: [标题] [副标题] [渐入时间] [停留] [渐出时间]`
  - 包含空格的标题文本应用 · 括起
- **Refresh** 动作现在可以指定图标槽位来刷新单个图标
- **ActionOption** 动作参数现在推荐使用 `{[TYPE]=[VALUE]}` 的格式定义
- **InputCatcher** 捕获器现在规范的参数包括 `type`, `start`, `cancel`, `end`. 特殊参数有 `display`, `itemLeft`, `itemRight`, `content` & 新增 BOOK 类型的捕获器

---

### 数据

- Meta & Data 功能模块重写，新增 GlobalData 用以设置全局变量
- 可通过 函数 功能调用

---

### 命令

- 物品仓库和物品转换合并为一个命令
- 音效预览功能基于 ReceptacleAPI 重写
- 性能损耗统计功能重写
- 菜单列表格式化
- 调试功能重写  
  Mirror & Dump 功能集成到 Debug 下

---

### API

- 提供 `ReceptacleAPI` 创建自定义虚拟容器，使用实例可参照 `me.arasple.mc.trmenu.module.internal.command.impl.CommandSounds`

