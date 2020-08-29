package me.arasple.mc.trmenu.api

import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.api.factory.MenuFactory
import me.arasple.mc.trmenu.modules.data.Metas
import me.arasple.mc.trmenu.modules.data.Sessions
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.display.menu.MenuLayout
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/18 8:48
 */
object Extends {

    fun CommandSender.sendLocale(path: String, vararg args: Any) {
        TLocale.sendTo(this, path, *args)
    }

    fun Player.replaceWithArguments(string: String): String {
        return Metas.replaceWithArguments(this, string)
    }

    fun Player.getArguments(): Array<String> {
        return Metas.getArguments(this)
    }

    fun Player.setArguments(args: Array<String>?) {
        Metas.setArguments(this, args)
    }

    fun Player.completeArguments(args: Array<String>) {
        Metas.completeArguments(this, args)
    }

    fun Player.setMeta(key: String, value: Any) {
        Metas.setMeta(this, key, value)
    }

    fun Player.setMeta(keys: Array<String>, value: Any) {
        keys.forEach { setMeta(it, value) }
    }

    fun Player.getMeta(key: String): Any? {
        return Metas.getMeta(this, key)
    }

    fun Player.getMetas(): MutableMap<String, Any> {
        return Metas.getMeta(this)
    }

    fun Player.removeMeta(key: String) {
        Metas.removeMeta(this, key)
    }

    fun Player.removeMeta(matcher: Metas.Matcher) {
        Metas.removeMeta(this, matcher)
    }

    fun Player.getMenuSession(): Menu.Session {
        return Sessions.getMenuSession(this)
    }

    fun Player.setMenuSession(menu: Menu?, layout: MenuLayout.Layout?, page: Int) {
        this.getMenuSession().set(menu, layout, page)
    }

    fun Player.getMenuFactorySession(): MenuFactory.Session {
        return Sessions.getMenuFactorySession(this)
    }

    fun Player.resetCache() {
        Metas.resetCache(this)
    }

    fun Player.removeMenuSession() {
        Sessions.removeMenuSession(this)
        Menu.getMenus().forEach { it.viewers.remove(this) }
    }

}