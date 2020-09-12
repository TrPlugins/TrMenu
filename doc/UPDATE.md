### Version 2.05
- Date: Sep 12, 2020
- Updates:
  - Updated license
  - Greatly improved page switch speed
  - Improved some performance issues
  - Fixed issue #178 (meta support in material)
  - Fixed issue #175, #179 (variable use in condition expression)
  - Fixed issue #187 (hopper layout support)
  - Fixed issue #189 (refresh icon display property)
  - Fixed issue #191 (`/trmenu item toJson` occure errors in 1.8)
  - Fixed issue #192 (inventory title color support in 1.8)
  - Fixed issue #194

### Version 2.04
- Date: Aug 30, 2020
- Updates:
  - Fixed hex color in inventory title
  - Fixed issue #162
  - Fixed issue #166 (meta resetting bug)
  - Fixed issue #165 (async chat to execute command bug)
  - Fixed issue #158 (sounds preview feature in 1.15.2)
  - Fixed issue #163 (improved texture skulls cache)
  - Fixed issue #170 (auto-reload issue with reopening)
  - Removed auto-install PlaceholderAPI feature
  - Added a cooldown for binding item use
  - Added command `dump` for debug information
  - Added command `mirror` for **performance monitoring**
  - Added support for format like `{#a1e1e6}`
  - Added gradients generate `{gradient:#55eb34,#34ebe5,#55eb34-}`
  - Added support for TokenManager
  - Added alias `repeat` for action retype (issue #168)
  - Added whitelist utils
  - The plugin will now close all menus when disabling
  - Update settings, with more customizable
  - Priority for icons is no longer required, the plugin will automatically descend them depends on configurations

### Version 2.03
- Date: Aug 20, 2020
- Updates:
  - Fixed player-head texture display
  - Fixed player-head load errors
  - Fixed economy hook errors with CMI
  - Fixed `ActionTakePoints.kt`
  - Fixed `ActionMetaRemove.kt`
  - Fixed SkinsRestorer, CMI hook
  - Added `setPlaceholders` for Assist Utils
  - Removed bracket placeholders parse feature in menu
  - Added locale `vi_VN` by Galaxy-VN
  - Update TabooLib Loader
  - Update NMS
  - Cleanup & improved code
  - Update [Offical Wiki](https://trmenu.trixey.cc/)
    - English by `Tanguygab`
    - Français by `Tanguygab`
    - ภาษาไทย by `The Night.`
    - Vietnamese by `GalaxyVN`

### Version 2.02
- Date: Aug 5, 2020
- Updates:
  - Fixed lots of icon erros
  - Fixed loading player head errors
  - Fixed argument/layout kept issue
  - Fixed script's placeholder parse issue
  - Fixed data placeholder `{data:Key}`
  - Fixed options combined issue with `&&&`
  - Fixed the `MenuEvents.Close` doesn't work
  - Added action `Reset` for resetting animation frames
  - Automatically identify and allocate update cycle for necessary properties

### Version 2.01
- Date: Aug 3, 2020
- Updates:
  - Fixed priority icons sorting issue
  - Fixed a catcher serialize issue
  - Fixed `F key` shortcut disabled offhand
  - Fixed actions execute in disorder
  - Fixed ActionTakeItem amount issue
  - You can now use `title` action easier, `title: MAINTITLE SUB\sTITLE 20 60 20`
  - Added `PlayerInventory-Border-Left/Right/Middle` shortcuts
  - Added `hasItem` condition check
  - Added `size`/`rows` support for those do not wish to use Layout feature
  - Updated Kotlin to 1.4.0-RC, and now support Forge server