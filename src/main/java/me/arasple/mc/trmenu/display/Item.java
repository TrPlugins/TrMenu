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
    private List<List<Integer>> rawSlots;
    private HashMap<UUID, List<List<Integer>>> slots;
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
        this.rawSlots = slots;
        this.itemFlags = itemFlags;
        this.shiny = shiny;
        this.amount = amount;
        this.finalShiny = Boolean.parseBoolean(shiny);
        this.finalAmount = NumberUtils.toInt(amount, -1);
        this.curSlots = new HashMap<>();
        this.slots = new HashMap<>();
        this.indexMap = new HashMap<>();
    }

    /**
     * 为一名玩家刷新该物品
     *
     * @param player 玩家
     * @param args   参数
     * @return 物品
     */
    public ItemStack createItemStack(Player player, String... args) {
        Mat mat = materials.get(nextIndex(player, 1));
        ItemStack itemStack = mat.createItem(player);
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
            itemStack.setAmount((int) NumberUtils.toDouble(String.valueOf(JavaScript.run(player, amount)), 1));
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

    /**
     * 下一个动态属性位置
     *
     * @param type 类型
     * @return 值
     */
    private int nextIndex(Player player, int type) {
        int size = type == 0 ? names.size() : type == 1 ? materials.size() : type == 2 ? lores.size() : getSlots(player).size();
        if (size == 1) {
            return 0;
        }
        int[] indexs = indexMap.computeIfAbsent(player.getUniqueId(), r -> new int[]{0, 0, 0, 0});
        indexs[type] = indexs[type] + 1 >= size ? 0 : indexs[type] + 1;
        return indexs[type];
    }

    private void resetIndex() {
        this.indexMap.clear();
    }

    public void resetIndex(Player player) {
        this.slots.clear();
        this.indexMap.remove(player.getUniqueId());
    }

    /*
    SLOTS
     */

    public List<List<Integer>> getSlots(Player player) {
        return slots.getOrDefault(player.getUniqueId(), getRawSlots());
    }

    public void setSlots(Player player, List<List<Integer>> slots) {
        this.slots.put(player.getUniqueId(), slots);
    }

    public List<Integer> getNextSlots(Player player, Inventory inv) {
        if (slots.size() > 0 && getCurSlots(player) != null) {
            getCurSlots(player).forEach(s -> inv.setItem(s, null));
        }
        setCurSlots(player, getSlots(player).get(nextIndex(player, 3)));
        return getCurSlots(player);
    }

    public List<Integer> getCurSlots(Player player) {
        return curSlots.getOrDefault(player.getUniqueId(), null);
    }

    public void setCurSlots(Player player, List<Integer> slots) {
        curSlots.put(player.getUniqueId(), slots);
    }

    /*
    GETTERS && SETTERS
     */

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Mat> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Mat> materials) {
        this.materials = materials;
    }

    public List<List<String>> getLores() {
        return lores;
    }

    public void setLores(List<List<String>> lores) {
        this.lores = lores;
    }

    public List<List<Integer>> getRawSlots() {
        return rawSlots;
    }

    public void setRawSlots(List<List<Integer>> rawSlots) {
        this.rawSlots = rawSlots;
    }

    public HashMap<UUID, List<List<Integer>>> getSlots() {
        return slots;
    }

    public void setSlots(HashMap<UUID, List<List<Integer>>> slots) {
        this.slots = slots;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public void setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getShiny() {
        return shiny;
    }

    public void setShiny(String shiny) {
        this.shiny = shiny;
    }

    public boolean isFinalShiny() {
        return finalShiny;
    }

    public void setFinalShiny(boolean finalShiny) {
        this.finalShiny = finalShiny;
    }

    public HashMap<UUID, List<Integer>> getCurSlots() {
        return curSlots;
    }

    public void setCurSlots(HashMap<UUID, List<Integer>> curSlots) {
        this.curSlots = curSlots;
    }

    public HashMap<UUID, int[]> getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(HashMap<UUID, int[]> indexMap) {
        this.indexMap = indexMap;
    }

}
