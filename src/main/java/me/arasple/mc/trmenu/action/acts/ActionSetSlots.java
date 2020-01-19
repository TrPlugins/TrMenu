package me.arasple.mc.trmenu.action.acts;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.data.ArgsCache;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/19 9:32
 * set-slot: 1,2,3;5;56;6
 */
public class ActionSetSlots extends AbstractAction {

    private List<List<Integer>> slots;

    @Override
    public String getName() {
        return "set(-)?slot(s)?";
    }

    @Override
    public void onExecute(Player player) {
        if (ArgsCache.getClickedItem().containsKey(player.getUniqueId())) {
            ArgsCache.getClickedItem().get(player.getUniqueId()).setSlots(player, slots);
        }
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
        if (getContent() == null) {
            return;
        }

        List<List<Integer>> slots = Lists.newArrayList();
        for (String group : getContent().split(";")) {
            List<Integer> integers = Lists.newArrayList();
            for (String slot : group.split(",")) {
                integers.add(Integer.valueOf(slot));
            }
            slots.add(integers);
        }

        this.slots = slots;
    }

}
