# TrMenu Ⅳ

### Dedicated for flexibility and versatility

---

### Note

Still under heavy development  
There is no estimated time of release

---

### Invero Preview

```kotlin
window(this, title = "Scroll Vertical") {

    scroll(8 to 6) {
        scrollVertically()
        scrollRandomType()

        for (i in 0..16)
            fillColum { buildItem<BasicItem>(generateRandomItem()) }

    }

    nav(1 to 6) {

        item(firstSlot(), Material.LIME_STAINED_GLASS_PANE, builder = { name = "§a← LEFT" }) {
            onClick { firstScrollPanel().previous() }
        }

        item(lastSlot(), Material.CYAN_STAINED_GLASS_PANE, builder = { name = "§a→ RIGHT" }) {
            onClick { firstScrollPanel().next() }
        }

        item(Material.GRAY_STAINED_GLASS_PANE).fillup()

    }

}.open()
```

Framework for advanced chest interface

https://user-images.githubusercontent.com/35389235/201510707-f52031fe-041a-43b2-9219-9fe4c1de9f7f.mp4

https://user-images.githubusercontent.com/35389235/202976806-7e4c1e3e-06ae-4e15-8752-5cda0227ee11.mp4

