package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 14:10
 */
public class Icon {

    private Item item;
    private HashMap<ClickType, List<AbstractAction>> actions;

    public Icon(Item item, HashMap<ClickType, List<AbstractAction>> actions) {
        this.item = item;
        this.actions = actions;
    }

    public Item getItem() {
        return item;
    }

    public HashMap<ClickType, List<AbstractAction>> getActions() {
        return actions;
    }

}
