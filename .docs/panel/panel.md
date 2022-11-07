# Panel

> An abstract form of GUI  
> With width, height


Concept `buildPanel` function

```
buildPanel<BasicPanel> { this@Window ->

    weight(PanelWeight.NORMAL)
    
    claim {
        absolute(0, 1, 2, 3, 4, 5, 6, 7, 8)
        absolute(25)
        layout(
            type = Layout.ABSOLUTE,
            layout = """"
                      #########,
                      ###XXX###,
                      ###XXX###,
                      ##BB#####,
                      #########,
                     """",
            collect = 'X', 'B'
        )
        
        applyRole(0, PositionRole.SLOT_ITEM)
        applyRole(25, PositionRole.SLOT_PANEL)
    }
    
    elements {
      buildElement<IconNormalItem> {
           static(true)
           item(ItemStack(Material.DIAMOND))
           distribute(
                buildDistributor<NormalDistributor> {
                  1, 2, 3
                }
           )
      }
    }
    
    onRender {
      
    }
    
    onInteract {
      
    }
    
    onDiscard {
    
    }
    
}
```

- Size (How many slos are this panel intended to occupy?)
- Distribute
    - Relative
        - positioning relatively from a parent?
    - Absolute
        - positioning regarding the Window
- Render
    - ItemProvider
    - Access to children's render
- Communicate
    - Transfer communications with children and parent
    - Send updates to the Window
    - Receive interactions from the Window

```
Page Properties

- Parent ?
- Children ?
- Size ? Caculatable width & height ?
- Elements
- Weight (render and interact priority)

Page Types

- Base Panel (elements)
    - Standard Panel (one group elements)
    - Generator Panel (auto-generator for elements)

- Paged Panel (unlimited elements in groups)

    # Static page amount
    
    - Paged Standard Panel (preset groups of elements)
    - Paged Tab Panel (preset groups of elements)
    - Paged Netesed Panel (preset groups of panels)
    
    # Dynamic page amount
 
    - Paged Generator Panel (auto groups)

- Scroll Panel (proxy window of panels)
    - Scroll Standard Panel
    - Scroll Generator Panel
    - Scroll Netesed Panel

- IO Panel
    - Storage Panel
    - Crafting Panel
```