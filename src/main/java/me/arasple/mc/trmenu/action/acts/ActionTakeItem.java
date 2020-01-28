package me.arasple.mc.trmenu.action.acts;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/14 10:50
 */
public class ActionTakeItem extends AbstractAction {

    @Override
    public String getName() {
        return "(take|remove)(-)?item(s)?";
    }

    @Override
    public void onExecute(Player player) {
        List<RequiredItem> items = getRequirements(player);
        if (items != null && !items.isEmpty()) {
            items.forEach(requiredItem -> requiredItem.takeItem(player));
        }
    }

    public List<RequiredItem> getRequirements(Player player) {
        List<RequiredItem> items = Lists.newArrayList();
        Arrays.stream(Vars.replace(player, getContent()).split(";")).forEach(item -> items.add(RequiredItem.valueOf(item)));
        return items;
    }

    /**
     * @author sky
     * @since 2018-06-13 18:27
     */
    public static class RequiredItem {

        private String material;
        private String name;
        private String lore;
        private Integer damage;
        private Integer amount;
        private Boolean hasName;
        private Boolean hasLore;

        RequiredItem(String material, String name, String lore, Integer damage, Integer amount, Boolean hasName, Boolean hasLore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.damage = damage;
            this.amount = amount;
            this.hasName = hasName;
            this.hasLore = hasLore;
        }

        boolean isRequired(ItemStack itemStack) {
            return (material == null || itemStack.getType().name().equalsIgnoreCase(material))
                    && (damage == -1 || Integer.valueOf(itemStack.getDurability()).equals(damage))
                    && (hasName == null || hasName.equals(itemStack.getItemMeta().hasDisplayName()))
                    && (hasLore == null || hasLore.equals(itemStack.getItemMeta().hasLore()))
                    && (name == null || (itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().contains(name)))
                    && (lore == null || (itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().toString().contains(lore)));
        }

        public boolean hasItem(Player player) {
            int checkAmount = amount;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && !itemStack.getType().equals(Material.AIR) && isRequired(itemStack)) {
                    checkAmount -= itemStack.getAmount();
                    if (checkAmount <= 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        void takeItem(Player player) {
            int takeAmount = amount;
            ItemStack[] contents = player.getInventory().getContents();
            for (int i = 0; i < contents.length; i++) {
                ItemStack itemStack = contents[i];
                if (itemStack != null && !itemStack.getType().equals(Material.AIR) && isRequired(itemStack)) {
                    takeAmount -= itemStack.getAmount();
                    if (takeAmount < 0) {
                        itemStack.setAmount(itemStack.getAmount() - (takeAmount + itemStack.getAmount()));
                        return;
                    } else {
                        player.getInventory().setItem(i, null);
                        if (takeAmount == 0) {
                            return;
                        }
                    }
                }
            }
        }

        public static RequiredItem valueOf(String source) {
            String material = null;
            String name = null;
            String lore = null;
            int amount = 1;
            int damage = -1;
            Boolean hasName = null;
            Boolean hasLore = null;
            for (String condition : source.split(",")) {
                String[] data = condition.split(":");
                if (data.length == 2) {
                    switch (data[0].toLowerCase()) {
                        case "material": {
                            material = data[1];
                            break;
                        }
                        case "name": {
                            name = data[1];
                            break;
                        }
                        case "lore": {
                            lore = data[1];
                            break;
                        }
                        case "amount": {
                            amount = NumberConversions.toInt(data[1]);
                            break;
                        }
                        case "damage": {
                            damage = NumberConversions.toInt(data[1]);
                            break;
                        }
                        case "isname":
                        case "hasname": {
                            hasName = Boolean.parseBoolean(data[1]);
                            break;
                        }
                        case "islore":
                        case "haslore": {
                            hasLore = Boolean.parseBoolean(data[1]);
                            break;
                        }
                        default:
                    }
                }
            }
            return new RequiredItem(material, name, lore, damage, amount, hasName, hasLore);
        }

    }

}
