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