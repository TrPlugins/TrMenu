package me.arasple.mc.trmenu.api.events;

import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:20
 */
public class MenuOpenEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean byConsole;
    private boolean cancelled;
    private Menu menu;

    public MenuOpenEvent(Player who, boolean byConsole, Menu menu) {
        super(who);
        this.byConsole = byConsole;
        this.menu = menu;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isByConsole() {
        return byConsole;
    }

    public void setByConsole(boolean byConsole) {
        this.byConsole = byConsole;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}