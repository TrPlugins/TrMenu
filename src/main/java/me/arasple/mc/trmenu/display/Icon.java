package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 14:10
 */
public class Icon {

    private Item item;
    private HashMap<ClickType, List<BaseAction>> actions;

    public Icon(Item item, HashMap<ClickType, List<BaseAction>> actions) {
        this.item = item;
        this.actions = actions;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public HashMap<ClickType, List<BaseAction>> getactions() {
        return actions;
    }

    public void setactions(HashMap<ClickType, List<BaseAction>> actions) {
        this.actions = actions;
    }

}
