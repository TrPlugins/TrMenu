package me.arasple.mc.trmenu.commands;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.utils.Hastebin;
import me.arasple.mc.trmenu.utils.Skulls;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * @author Arasple
 * @date 2020/1/20 16:30
 * template <ROWS>
 */
public class CommandTemplate extends BaseSubCommand {

    private static List<Character> keys = Arrays.asList('#', '-', '+', '|', '=', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Rows", false, () -> Arrays.asList("1", "2", "3", "4", "5", "6"))};
    }


    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        int rows = Math.min(args.length >= 1 ? NumberUtils.toInt(args[0], 5) : 3, 6);
        MenuBuilder.builder()
                .title("TrMenu Template")
                .rows(rows)
                .items(getEmptyLines(rows))
                .close(e -> {
                    Player player = (Player) e.getPlayer();
                    Inventory inventory = e.getInventory();
                    if (countInvItems(inventory) <= 0) {
                        TLocale.sendTo(player, "TEMPLATE.EMPTY");
                        return;
                    }
                    Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                        TLocale.sendTo(player, "TEMPLATE.PROCESSING");
                        TemplateMenu menu = inventoryAsTrMenu(inventory);
                        String url = Hastebin.paste(menu.toYaml());
                        TLocale.sendTo(player, url != null ? "TEMPLATE.SUCCESS" : "TEMPLATE.FAILED", url);
                    });
                })
                .lockHand(false)
                .open((Player) sender);
    }

    private String[] getEmptyLines(int rows) {
        String[] strings = new String[rows];
        Arrays.fill(strings, "");
        return strings;
    }

    private TemplateMenu inventoryAsTrMenu(Inventory inventory) {
        HashMap<ItemStack, List<Integer>> items = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (Items.isNull(item)) {
                continue;
            }
            items.putIfAbsent(item, new ArrayList<>());
            items.get(item).add(i);
        }

        return new TemplateMenu("TrMenu Template", inventory.getSize() / 9, items);
    }

    public static class TemplateMenu {

        private String name;
        private int rows;
        private HashMap<ItemStack, List<Integer>> items;

        public TemplateMenu() {
        }

        public TemplateMenu(String name, int rows, HashMap<ItemStack, List<Integer>> items) {
            this.name = name;
            this.rows = rows;
            this.items = items;
        }

        public String toYaml() {
            YamlConfiguration yaml = new YamlConfiguration();
            List<String> shape = Lists.newArrayList();
            ListIterator<Character> key = keys.listIterator();

            for (int i = 0; i < rows; i++) {
                shape.add("         ");
            }
            yaml.set("Title", name);
            yaml.set("Shape", shape);
            items.forEach((item, slots) -> {
                char k = key.next();
                slots.forEach(s -> put(shape, s, k));
                yaml.set("Buttons." + k + ".display", display(item));
            });

            return yaml.saveToString();
        }

        private ConfigurationSection display(ItemStack item) {
            ConfigurationSection section = new MemoryConfiguration();
            ItemMeta meta = item.getItemMeta();
            String mat = item.getType().name();
            if (meta instanceof SkullMeta) {
                String texture = Skulls.getTexture(item);
                mat = texture != null ? "<skull:" + Skulls.getTexture(item) + ">" : mat;
            }
            if (item.getAmount() > 1) {
                section.set("amount", item.getAmount());
            }
            if (meta != null) {
                if (meta.hasDisplayName()) {
                    section.set("name", meta.getDisplayName());
                }
                if (meta.hasLore()) {
                    section.set("lore", meta.getLore());
                }
            }
            section.set("mats", mat);
            return section;
        }

        private void put(List<String> shape, int slot, char key) {
            int line = 0;
            slot++;
            while (slot > 9) {
                slot -= 9;
                line++;
            }
            slot--;
            char[] modify = shape.get(line).toCharArray();
            modify[slot] = key;
            shape.set(line, new String(modify));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public HashMap<ItemStack, List<Integer>> getItems() {
            return items;
        }

        public void setItems(HashMap<ItemStack, List<Integer>> items) {
            this.items = items;
        }

    }

    private int countInvItems(Inventory inventory) {
        int count = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (!Items.isNull(stack)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

}
