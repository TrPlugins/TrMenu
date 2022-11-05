# Invero Concepts

---

```text
Window can have only one viewer  
Panels can be children of many windows  
```

### Window

> Provider of vanlian container  
> Bukkit inventory and packet-based utilties

- InvenroryTypes:
    - Chest/Hoper...
    - Anvil (Text Input)
- WindowTypes:
    - Inventory

### Panel

> An abstract form of GUI  
> With width, height

Page Properties

- Parent ?
- Children ?
- Size ? Caculatable width & height ?
- Elements
- Weight (render and interact priority)

Page Types

- Base Panel
    - Standard Panel
    - Generator Panel

- Paged Panel
    - Paged Standard Panel
    - Paged Generator Panel
    - Paged Scroll Panel
    - Paged Tab Panel
    - Paged Netesed Panel

- IO Panel
    - Storage Panel
    - Crafting Panel

### Slot

- SLOT_EMPTY
- SLOT_ITEM
- SLOT_IO
- SLOT_ELEMENT
- SLOT_CHILD
- SLOT_PARENT

### Animation

> Played for Panel with PacketOutWindowItems   
> When playing an animation, no interaction is allowed  

