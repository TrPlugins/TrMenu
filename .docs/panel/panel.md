# Panel

> An abstract form of GUI  
> No width, height. Only size(slots)

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
```