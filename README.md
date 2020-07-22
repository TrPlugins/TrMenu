# TrMenu

---

Discord: https://discord.gg/8CWa6KF  
Wiki: https://trmenu.trixey.cn  
Wiki en Español(FanMade): https://trmenu.minelatino.com

---

TrMenu was released on Oct,4, 2019, aiming to be the new modern menu plugin for Minecraft servers  
And this is a now completly recoded premium menu plugin made with [TabooLib5](https://github.com/TabooLib)

---
#### Versions

- TrMenu ([v1.x](https://github.com/Arasple/TrMenu/tree/master))
  - Free normal version
  - Suspend updates and support

- TrMenu Premium (v2.x) **Developing**
  - Open-Source Premium version
  - Recoded

---

#### Features

### Performance
 - TrMenu v2 is recoded with Kotlin
 - Inventorys and items are completely based on async packets. No lag.

### Efficient & Simple
 - TrMenu v2's menu config support the normal version, however, there are a lot of changes
 - It is now much simpler to configure icons and menu options, you can still enjoy multiple nodes & ignore case, etc.

DeluxeMenus (67 lines)
```YAML
items: 
  'cooldown':
    material: ORANGE_CONCRETE
    slot: 13
    priority: 1
    update: true
    view_requirement:
      requirements:
        permission:
          type: has permission
          permission: essentials.kits.example
        available:
          type: string equals ignorecase
          input: '%essentials_kit_is_available_example%'
          output: 'no'
    display_name: ' '
    lore:
    - "&8• &bKit: &7Example"
    - "&8• &bStatus: &7On Cooldown"
    - "&8• &bAvailable In: &7%essentials_kit_time_until_available_example%"
    - ""
    left_click_commands:
    - "[refresh]"
    right_click_commands:
    - "[refresh]"
  'available':
    material: LIME_CONCRETE
    slot: 13
    priority: 2
    update: true
    view_requirement:
      requirements:
        permission:
          type: has permission
          permission: essentials.kits.example
        available:
          type: string equals ignorecase
          input: '%essentials_kit_is_available_example%'
          output: 'yes'
    display_name: ' '
    lore:
    - "&8• &bKit: &7Example"
    - "&8• &bStatus: &7Available"
    - ""
    - "&8» &b&nClick to Claim"
    - ''
    left_click_commands:
    - '[player] essentials:kit example'
    - '[refresh]'
    - '[refresh]<delay=100>'
    right_click_commands:
    - '[player] essentials:kit example'
    - '[refresh]'
    - '[refresh]<delay=100>'
  'locked':
    material: RED_CONCRETE
    slot: 13
    priority: 3
    update: true
    display_name: ' '
    lore:
    - "&8• &bKit: &7Example"
    - "&8• &bStatus: &7Locked"
    - ""
    left_click_commands:
    - "[close]"
    right_click_commands:
    - "[close]"
```

TrMenu (40 lines, -41%)
```YAML
Icons: 
  Kit:
    refresh: 20
    display:
      material: LIME_CONCRETE
      name: ' '
      lore:
      - "&8• &bKit: &7Example"
      - "&8• &bStatus: &7Available"
      - ""
      - "&8» &b&nClick to Claim"
      - ''
    actions:
      left,right:
      - 'player: essentials:kit example'
      - 'refresh'
      - 'refresh<delay:100>'
    icons:
      - condition: 'noPerm.essentials.kits.example'
        priority: 1
        display:
          material: RED_CONCRETE
          display_name: ' '
          lore:
          - "&8• &bKit: &7Example"
          - "&8• &bStatus: &7Locked"
          - ""
        actions:
          left,right: 'close'
      - condition: 'hasPerm.essentials.kits.example and is.%essentials_kit_is_available_example%.no'
        priority: 0
        display:
          material: ORANGE_CONCRETE
          display_name: ' '
          lore:
          - "&8• &bKit: &7Example"
          - "&8• &bStatus: &7On Cooldown"
          - "&8• &bAvailable In: &7%essentials_kit_time_until_available_example%"
          - ""
        actions:
          left,right: 'refresh'
```

### Menu Events
 - You can customize your own actions for menu events more fully  
 - Such as `MenuClickEvent`, `MenuCloseEvent`, `MenuClickEvent`
 
### Menu Tasks
 - You can create period tasks with reactions  
 - For players after opening the menu  

### Internal Functions
 - Simpler way for custom script & variables
 - You can use it anywhere in the menu `${id}` 

### Reactions
 - Pack mutiple actions up with priority and conditions
 - Which you can use in `Icon.ClickActions` & `MenuEvents` & `Requirement` or `Tasks`

### Player Inventory as menu
 - You can now use player's inventory as extra slots
 - Setup icons & etc.

### Menu ClickTypes
 - You can now enjoy a greater variety of click types

### Smart Expressions
 - Much simpler expressions for requirement use
 - `player.hasPermission("demo") && %vault_eco_balance% >= 500`
 - `hasPerm.demo and hasMoney.500`
 - You can also add your own expression pattern easily

### Cached Scripts
 - Faster java scripts

### Hex Color (1.16+)
 - You can use hex color easily anywhere in the menu
 - Format `&<FFFFFF>` or `&<256,256,256>`

### Highly Customizable
 - Based on TLocale(by TabooLib), support mutiple languages
 - You can custom almost everything you see of this plugin
 - Just take a look at locale/zh_CN.yml

### Exporter
  - To locale YAML file
  - Paste on Hastebin

### Developer API
  - `MenuFactory` allows you to create an advanced gui quickly
  - Also, completely based on packets
  - and more

### Much more

---

### Release Date

Not sure yet

---
