# TrMenu Update Logs #

#### VERSION 1.1
  - ##### 1.15
    - Since: 2020.1.27
    - Updates:
      - Fixe double value can not be cast to int for dynamic item amount
      - Fixed tag `<variable>` doesn't work
      - Added `KEEP-OPEN-REQUIREMENT` option. Periodically check this condition and close the menu if it is not met. For example: `"%vault_eco_balance%" > 1000`, `%vault_rank% == "VIP";20` <--- Period: 20ticks
      - Added `getItemBuildr()` for TrUtils
      - Added multiple shapes support, creating multi-page menus has never been easier [#20](https://github.com/Arasple/TrMenu/issues/20)
      - Added action set shape to swith menu shape. Usage `set-shape: 0`, or `shape: 1`, variable `{page}` for shape index + 1
      - Added bStats for auto updater statiscs
      - If you set an icon's slot below 0, it will auto-update to the first empty slot of the inventory
      - (For Dev.) Now you can add your custom object to TrMenu's JavaScript check by
        `JavaScript.getBindings().put(String key, Object object)`
  - ##### 1.14
    - Since: 2020.1.24
    - Updates:
      - Fixed title action NullPointerException when don't set subtitle content
      - Fixed arguments can not be used in `Open-Requirement`/`Open-Deny-Actions`
      - Simplified the update checker & its notify message
      - Added config updater for settings.yml
      - Added auto-updater (default disabled) to auto-update TrMenu
      - Added command `/trmenu update` to update the plugin easily
      - Added js support for Mat, for example
        `<js-item:player.getInventory().getArmorContents()[3]>`
      - Added right-click player shortcut bind
      - Added default example menu `profile.yml`
      - You can now use items from Oraxen like `'<oraxen:ID>'`
  - ##### 1.13
    - Since: 2020.1.20
    - Updates:
      - Added locale vi_VN translated by Galaxyvietnam
      - Added locale th_TH translated by DriteStudio
      - Added action ForceClose to close menu without running close actions
      - Added option.silent to disable the loading logo
      - Added template feature, generate menu conveniently
      - Added command /trmenu item to translate item into JSON,
        which you can use in Mats, NBT is supported
      - Animated title is now supported :)
      - Fixed refresh priority icons node issue
      - R1
        - DeluxeMenus fully migrate is now supported
      - R2
        - Fixed animated title buggy in versions below 1.14
        - Supported to migrate menus in config.yml of DeluxeMenus
        - Improved animated titles
      - R3
        - Update depend TabooLib to 5.15
        - Added locale Spanish translated by brs73
        - 'Shape' is no longer mandatory demand
        - Added `parseBracketPlaceholders` for TrUtils
        - Added `OPTIONS.CATCHER-CANCEL-WORDS` in `settings.yml` to define custom words cancel a catcher action
        - You can now define `ROWS` or `SIZES` in menu if you dont use `Shape`
        - Give/Take money action support variables now
        - Added bStats for Menu-Item types, inventory types
        - Fixed NBT item to json bug
        - Fixed a template issue when you put too many items at once
        - Fixed NullPointerException in share command
        - Fixed Hastebin paste encode issue
        - Added json to item command (`/trmenu item <json>`)
        - Added offhand (or offhand while sneaking ) open menu bind for 1.9+ 
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
      - Improved ActionCommand(Player/OP/Console)
      - Added ActionGiveMoney
      - Added ActionTakeMoney 
      - Added ActionTakeItem (Remove specify items)
      - Now you can create internal menus in settings.yml
      - Added Traditional Chinese locale file (zh_TW)
      - R2
        - Added MenuOptions.Update-Inventory
        - Fixed animation buggy when there are multiple viewers
        - Added TrUtils, can be used in JavaScript
        - Recoded "Mat"
        - Added <dye: r,g,b> for create dyed leather armor
        - Added <banner: > for create dyed custom banner
      - R3
        - Added ChatCatcher, for example
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
        - Fixed menu file listener
        - Added notify message when auto-reload menu failed
        - Each icon's priority can be defined with "priority: [Num]" now
        - Fixed a bStats error
        - Improved item's ID reading
        - Now supports materials of Mods in Forge server
      - R5
        - Support .json file menu load
        - Fixed animate slots buggy when there're multiple viewers
        - Added variable $openBy which returns CONSOLE or PLAYER
      - R6
        - Added getPlayerArgs for TrUtils
        - Fixed a possible way of moving items into menu bug
        - Fixed input catcher via sign does not work
        - Fixed input catcher load error
        - Added Refresh action to recalculate all the priority icons
        - Added Set Args action to re-set arguments
      - R7
        - Added SetSlots action
        - Fixed JavaScript can not use clickEvent, clickItemStack etc.
        - Improved animated slots
        - Added ClearEmptySlots action
        - Inventory DISPENSER and HOPPER can now use shape to compose
        - Added a debug message to display click raw slot
  - ##### 1.11
    - Since: 2020.1.12
    - Updates:
      - R5
        - Improved "about" GUI
        - Add TrMenu PlaceholderAPI variables
