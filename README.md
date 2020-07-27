# TrMenu

##### Modern & Advanced Menu-Plugin for Minecraft Servers

![](https://img.shields.io/github/last-commit/Arasple/TrMenu?logo=artstation&style=for-the-badge&color=9266CC)![](https://img.shields.io/github/issues/Arasple/TrMenu?style=for-the-badge&logo=slashdot)![](https://img.shields.io/github/release/Arasple/TrMenu?style=for-the-badge&color=00C58E&logo=ionic)![](https://img.shields.io/github/downloads/Arasple/TrMenu/total?style=for-the-badge&logo=docusign)

---

Discord : https://discord.gg/8CWa6KF

Wiki: [https://trmenu.trixey.cn](https://trmenu.trixey.cn/)

---

**Introduce**

​	TrMenu was released on Oct,4 2019, aiming to be the new modern menu plugin for Minecraft servers. Currently developing TrMenu v2 is a completely recoded version with more features

​	TrMenu is using powerful *Built-in Bukkit-API Expansion* [TabooLib5](https://github.com/TabooLib)

---

**Features**

- **Optimized performance** : An extremely optimized performance is ensured.
  Completely packet-based inventory & update only when necessary
- **Effective and flexibility** : Aliases nodes, ignore cases, auto-reload function and template support, aims to improve the efficiency of creating menus
- **Simple** : Create a most advanced menu with the least lines. No need for knowledge of scripting. The plugin is easy to use and menu configuration has a clear structure of every component
- **Plentiful functions** : Animated titles & items, paged menus, arguments, editable meta, and storable data for players, use player inventory as extra slots for menus, also a great variety of click-actions and so much more while the only limit is your imagination
  - **Animated menu** : You can have animated title, animated name & lore & amount and so on for icon item
  - **Conditional icons** : Sub icons for each icon with different priority and requirement are allowed
  - **Player Inventory** : Player inventory `4*9 ` are allowed to use as extra slots when open the menu
  - **Reactions** : Almost everywhere, you can use reactions each of which are composed of priority, condition, actions, and deny-actions
  - **Click types** : All Minecraft inventory click types are supported, including not only number keys from 1-9 and switch offhand key
  - **Smart expressions** : From `player.hasPermission("demo") && %vault_eco_balance% >= 500` to `hasPerm.demo and hasMoney.500`
  - **Arguments** : Open a menu with arguments, which can be used as variables anywhere in the menu with the format of `{<Index>}`, for example, `{0}` for the first argument
  - **Meta mark** : You can set editable meta (key & value) for each player, which can be use as variables
  - **Cached scripts** : Some cacheable scripts will be cached for better performance
  - **PlaceholderAPI** : Completely support, and you can use it everywhere
  - **Hex color** : For 1.16+ servers you can use hex or rgb color easily `&{FFFFFF}, &{256,256,256}`
  - Much more ...
- **Developer API**  : We have provided different menu events and `TrMenuAPI`, also `MenuFactory` to build packeted-based GUIs for your plugin quickly

---

**bStats**

![bStats](https://bstats.org/signatures/bukkit/TrMenu.svg)

---

**Build**

1. Clone this project to your IDEA
2. Execute Gradle task `buildJar`

---

**Terms**

1. You are not allowed to distribute or resell any part of this plugin to anyone
2. You are only allowed to use this plugin on your own servers
3. You are not allowed to claim any part of this plugin as your own
4. You are not allowed to repost this plugin on anywhere without asking permission
5. You are not allowed to request a refund for any reason

---