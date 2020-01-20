# TrMenu Update Logs #

#### VERSION 1.1
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
