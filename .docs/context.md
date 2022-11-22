[翻页]

- PagedStandardPanel（有静态占据元素）
- PagedGeneratorPanel（已实现静态占据元素：排除生成池）

- PagedNetesedPanel（无静态占据元素：未实现应用池）
- ScrollGeneratorPanel（未实现静态占据元素：未实现生成池）
- ScrollStandardPanel（未实现静态占据元素：未实现滚动池）

### BasePanel

- StandardPanel
    - Noraml GUI
    - Accept ElementAbsolute & ElementDynamic (MappedElements)

### BaseScrollPanel

- ScrollStandardPanel
    - Static Scroll GUI
    - Accept only ElementAbsolute (ScrollColum)

### BasePagedPanel

- PagedStandardPanel
    - Standard Paged GUI, Eqauls to mutiple standard panel
    - Accept default elements
    - Accept ElementAbsolute & ElementDynamic (MappedElements)

- PagedGeneratorPanel
    - Standard Generator GUI
    - Accept default elements to exclude generate pool slots
    - Accept only ElementAbsolute


- PagedNetesedPanel
    - Netesed Paenl
    - Theoretically accepts all types of panel

### BaseIOPanel

- IOStoragePanel
    - Sepcial element type, only occupies slots
    - ItemStack storage, interact support