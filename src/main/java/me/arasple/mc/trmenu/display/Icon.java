package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.utils.Notifys;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 14:10
 */
public class Icon {

    private int priority;
    private String requirement;
    private Item item;
    private HashMap<ClickType, List<AbstractAction>> actions;

    public Icon(int priority, String requirement, Item item, HashMap<ClickType, List<AbstractAction>> actions) {
        this.priority = priority;
        this.requirement = requirement;
        this.item = item;
        this.actions = actions;
    }

    public int getPriority() {
        return priority;
    }

    public String getRequirement() {
        return requirement;
    }

    public Item getItem() {
        return item;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public HashMap<ClickType, List<AbstractAction>> getActions() {
        return actions;
    }

    public void onClick(Player player, Button button, ClickType clickType, InventoryClickEvent event) {
        List<AbstractAction> actions = getActions().getOrDefault(clickType, new ArrayList<>());
        Notifys.debug(player, "ClickType: {0}, Actions: {1}. &8{2}", clickType.name(), actions.size(), actions);
        ArgsCache.getEvent().put(player.getUniqueId(), event);
        ArgsCache.getClickedItem().put(player.getUniqueId(), item);

        TrAction.runActions(actions, player);
        if (getActions().get(null) != null) {
            TrAction.runActions(getActions().get(null), player);
        }

        ArgsCache.getEvent().remove(player.getUniqueId());
        ArgsCache.getClickedItem().remove(player.getUniqueId());

        button.refreshConditionalIcon(player, event);
    }

}
