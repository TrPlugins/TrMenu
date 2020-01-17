package me.arasple.mc.trmenu.display;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 15:39
 */
public class Item {

    private List<String> names;
    private List<Mat> materials;
    private List<List<String>> lores;
    private List<List<Integer>> slots;
    private List<ItemFlag> itemFlags;
    private String amount;
    private int finalAmount;
    private String shiny;
    private boolean finalShiny;
    private HashMap<UUID, List<Integer>> curSlots;
    private HashMap<UUID, int[]> indexMap;

    public Item(List<String> names, List<Mat> materials, List<List<String>> lores, List<List<Integer>> slots, List<ItemFlag> itemFlags, String shiny, String amount) {
        this.names = names;
        this.materials = materials;
        this.lores = lores;
        this.slots = slots;
        this.itemFlags = itemFlags;
        this.shiny = shiny;
        this.amount = amount;
        this.finalShiny = Boolean.parseBoolean(shiny);
        this.finalAmount = NumberUtils.toInt(amount, -1);
        this.curSlots = new HashMap<>();
        resetIndex();
    }

    /**
     * 为一名玩家刷新该物品
     *
     * @param player 玩家
     * @param args   参数
     * @return 物品
     */
    public ItemStack createItemStack(Player player, String... args) {
        ItemStack itemStack = materials.get(nextIndex(player, 1)).createItem(player);
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        if (lores.size() > 0) {
            itemMeta.setLore(Vars.replace(player, lores.get(nextIndex(player, 2))));
        }
        if (names.size() > 0) {
            itemMeta.setDisplayName(Vars.replace(player, names.get(nextIndex(player, 0))));
        }
        if (finalShiny || (boolean) JavaScript.run(player, shiny)) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemFlags.add(ItemFlag.HIDE_ENCHANTS);
        }
        if (finalAmount != -1) {
            itemStack.setAmount(finalAmount);
        } else {
            itemStack.setAmount(NumberUtils.toInt(String.valueOf(JavaScript.run(player, amount)), 1));
        }
        if (itemFlags.size() > 0) {
            itemFlags.forEach(itemFlag -> {
                if (!itemMeta.hasItemFlag(itemFlag)) {
                    itemMeta.addItemFlags(itemFlag);
                }
            });
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public List<List<Integer>> getSlots() {
        return slots;
    }

    public List<Integer> getNextSlots(Player player, Inventory inv) {
        if (slots.size() > 0 && getCurSlots(player) != null) {
            getCurSlots(player).forEach(s -> inv.setItem(s, null));
        }
        setCurSlots(player, slots.get(nextIndex(player, 3)));
        return getCurSlots(player);
    }

    public List<Integer> getCurSlots(Player player) {
        return curSlots.getOrDefault(player.getUniqueId(), null);
    }

    public void setCurSlots(Player player, List<Integer> slots) {
        curSlots.put(player.getUniqueId(), slots);
    }

    /**
     * 下一个动态属性位置
     *
     * @param type 类型
     * @return 值
     */
    private int nextIndex(Player player, int type) {
        int[] indexs = indexMap.computeIfAbsent(player.getUniqueId(), r -> new int[]{0, 0, 0, 0});
        int size = 0;
        switch (type) {
            case 0:
                size = names.size();
                break;
            case 1:
                size = materials.size();
                break;
            case 2:
                size = lores.size();
                break;
            case 3:
                size = slots.size();
                break;
            default:
                break;
        }
        if (size == 1) {
            return 0;
        }
        int i = indexs[type];
        if (i + 1 >= size) {
            indexs[type] = 0;
        } else {
            indexs[type]++;
        }
        return i;
    }

    private void resetIndex() {
        if (indexMap == null) {
            indexMap = new HashMap<>();
            return;
        }
        this.indexMap.clear();
    }

    public void resetIndex(Player player) {
        this.indexMap.remove(player.getUniqueId());
    }

    /*
    GETTERS
     */

    public List<String> getNames() {
        return names;
    }

    public List<Mat> getMaterials() {
        return materials;
    }

    public List<List<String>> getLores() {
        return lores;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public String getAmount() {
        return amount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public String getShiny() {
        return shiny;
    }

    public boolean isFinalShiny() {
        return finalShiny;
    }

    public HashMap<UUID, int[]> getIndexMap() {
        return indexMap;
    }

}
