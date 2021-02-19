package me.arasple.mc.trmenu.module.display

import me.arasple.mc.trmenu.api.event.MenuOpenEvent
import me.arasple.mc.trmenu.api.event.MenuPageChangeEvent
import me.arasple.mc.trmenu.api.receptacle.window.Receptacle
import me.arasple.mc.trmenu.module.display.icon.Icon
import me.arasple.mc.trmenu.module.display.layout.MenuLayout
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.service.Performance
import me.arasple.mc.trmenu.util.Tasks
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/24 10:01
 */
class Menu(
    val id: String,
    val settings: MenuSettings,
    val layout: MenuLayout,
    val icons: Set<Icon>
) {

    companion object {

        val menus = mutableListOf<Menu>()

    }

    val viewers: MutableSet<String> = mutableSetOf()

    /**
     * 开启菜单
     */
    fun open(
        viewer: Player,
        page: Int = settings.defaultLayout,
        reason: MenuOpenEvent.Reason,
        block: (MenuSession) -> Unit = {}
    ) {
        Performance.MIRROR.check("Menu:Event:Open") {
            val session = MenuSession.getSession(viewer).also(block)
            viewers.add(viewer.name)

            if (session.menu == this) {
                return page(viewer, page)
            } else if (session.menu != null) {
                session.shut()
            }

            if (MenuOpenEvent(session, this, page, reason).call().isCancelled) return

            if (Metadata.byBukkit(viewer, "FORCE_OPEN") || settings.openEvent.eval(session)) {
                val layout = layout[page]
                val receptacle: Receptacle

                session.menu = this
                session.page = page
                session.receptacle = layout.baseReceptacle().also { receptacle = it }
                session.playerItemSlots()

                layout.initReceptacle(session)
                loadTitle(session)
                loadIcon(session)
                loadTasks(session)

                receptacle.open(viewer)
            }
        }
    }

    /**
     * 本菜单内切换页码
     */
    fun page(viewer: Player, page: Int) {
        Performance.MIRROR.check("Menu:Event:ChangePage") {
            val session = MenuSession.getSession(viewer)

            val previous = session.layout()!!
            val layout = layout[page]
            val receptacle: Receptacle
            val override = previous.isSimilar(layout)

            if (MenuPageChangeEvent(session, session.page, page, override).call().isCancelled) return
            if (override) {
                receptacle = session.receptacle!!
                receptacle.clearItems()
            } else {
                session.receptacle = layout.baseReceptacle().also { receptacle = it }
                layout.initReceptacle(session)
            }

            session.page = page
            session.playerItemSlots()
            loadIcon(session)

            if (override) receptacle.refresh(viewer)
            else receptacle.open(viewer)
        }
    }

    /**
     * 加载容器标题 & 自动更新
     */
    private fun loadTitle(session: MenuSession) {
        val setTitle = {
            session.receptacle?.title = settings.title.next(session.id)?.let { session.parse(it) } ?: "TrMenu"
        }.also { it.invoke() }

        if (settings.titleUpdate > 0) {
            session.arrange(Tasks.timer(10, settings.titleUpdate.toLong(), true) {
                Performance.MIRROR.check("Menu:Title:Update") {
                    setTitle.invoke()
                }
            })
        }
    }

    private fun loadIcon(session: MenuSession) {
        session.shutTemps()
        icons
            .forEach {
                it.position.reset(session)

                if (it.isAvailable(session)) {
                    try {
                        it.onRefresh(session)
                        it.startup(session)
                        session.activeIcons.add(it)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        println("ICON: ${it.id}")
                    }
                }
            }
    }

    private fun loadTasks(session: MenuSession) {
        settings.tasks.forEach { (period, reactions) ->
            session.arrange(
                Tasks.timer(5L, period, true) {
                    Performance.MIRROR.check("Menu:CustomTasks") {
                        reactions.eval(session)
                    }
                }
            )
        }
    }

    fun getIcon(id: String): Icon? {
        return icons.find { it.id == id }
    }

    fun forViewers(block: (Player) -> Unit) {
        viewers.mapNotNull { Bukkit.getPlayerExact(it) }.forEach(block)
    }

    fun forSessions(block: (MenuSession) -> Unit) {
        forViewers { block(MenuSession.getSession(it)) }
    }

    /**
     * PRIVATE
     */
    fun isFreeSlot(slot: Int): Boolean {
        return settings.freeSlots.contains(slot)
    }

    fun removeViewer(viewer: Player) {
        viewers.remove(viewer.name)
    }

    fun removeViewers() {
        viewers.clear()
    }

}