package me.arasple.mc.trmenu.migrate;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.commands.CommandMigrate;
import me.arasple.mc.trmenu.utils.TrUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2020/1/21 17:04
 */
public class DeluxeMenusMigrater {

    public static void migrateDeluxeMenu(File file) {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
        if (c.contains("gui_menus")) {
            for (String menu : c.getConfigurationSection("gui_menus").getKeys(false)) {
                migrateDeluxeMenu(null, menu + ".yml", c.getConfigurationSection("gui_menus." + menu));
            }
        } else {
            migrateDeluxeMenu(file, file.getName(), c);
        }
    }

    public static void migrateDeluxeMenu(File file, String menu, ConfigurationSection dm) {
        try {
            YamlConfiguration tm = new YamlConfiguration();
            ListIterator<Character> k = TrUtils.getKeys().listIterator();
            tm.options().header(
                    " " + "\n" +
                            "Migrated from DeluxeMenus, by TrMenu" + "\n" +
                            "Date: " + new Date().toString() + "\n" +
                            " " + "\n"
            );
            List<String> shape = getEmptyShape(dm.getInt("size", 54));
            int menuUpdate = dm.getInt("update_interval", 1) * 20;
            tm.set("Title", dm.getString("menu_title", "TrMenu"));
            tm.set("Shape", shape);
            tm.set("Open-Commands", dm.get("open_command"));
            if (dm.isSet("open_requirement.requirements")) {
                tm.set("Open-Requirement", migrateRequires(dm.getConfigurationSection("open_requirement.requirements")));
            }
            if (dm.isSet("open_requirement.deny_commands")) {
                tm.set("Open-Deny-Actions", migrateIconCommands(dm.getStringList("open_requirement.deny_commands")));
            } else if (dm.contains("open_requirement.requirements")) {
                dm.getConfigurationSection("open_requirement.requirements").getKeys(false).forEach(path -> dm.getConfigurationSection("open_requirement.requirements." + path).getKeys(false).forEach(key -> {
                    if ("deny_commands".equals(key)) {
                        tm.set("Open-Deny-Actions", migrateIconCommands(dm.getStringList("open_requirement.requirements." + path + "." + key)));
                    }
                }));
            }
            HashMap<List<Integer>, List<DmIcon>> icons = new HashMap<>();
            List<DmIcon> dmIcons = Lists.newArrayList();

            dm.getConfigurationSection("items").getValues(false).values().forEach(i -> {
                MemorySection icon = (MemorySection) i;
                // Icon priority
                int priority = icon.getInt("priority", Integer.MAX_VALUE);
                String requirement = icon.isSet("view_requirement.requirements") ? migrateRequires(icon.getConfigurationSection("view_requirement.requirements")) : null;
                List<Integer> slots = icon.contains("slots") ? icon.getIntegerList("slots") : Collections.singletonList(icon.getInt("slot"));
                // Display part
                int iconUpdate = icon.getBoolean("update", false) ? menuUpdate : -1;
                String material = migrateMaterial(icon);
                String displayName = icon.getString("display_name");
                String amount = icon.getString("amount", icon.getString("dynamic_amount"));
                String shiny = String.valueOf(icon.contains("enchantments"));
                List<String> lore = icon.getStringList("lore");
                List<String> flags = migrateFlags(icon);
                // Actions Part
                HashMap<String, List<String>> actions = migrateIconActions(icon);
                dmIcons.add(new DmIcon(priority, requirement, iconUpdate, slots, material, displayName, lore, amount, shiny, flags, actions));
            });
            dmIcons.forEach(i -> {
                icons.putIfAbsent(i.getSlots(), Lists.newArrayList());
                icons.get(i.getSlots()).add(i);
            });
            icons.forEach((slots, i) -> {
                ConfigurationSection button = new MemoryConfiguration();
                char key = k.next();
                applySlots(shape, slots, key);
                int update = i.stream().min(Comparator.comparingInt(DmIcon::getUpdate)).get().getUpdate();
                int refresh = i.size() > 1 ? 20 : -1;
                if (update > 0) {
                    button.set("update", update);
                }
                if (refresh > 0) {
                    button.set("refresh", refresh);
                }
                if (i.size() == 1) {
                    for (Map.Entry<String, Object> entry : i.get(0).toTrMenuSection().getValues(false).entrySet()) {
                        button.set(entry.getKey(), entry.getValue());
                    }
                } else {
                    DmIcon def = i.stream().filter(x -> x.getRequirement() == null || x.getPriority() == Integer.MAX_VALUE).findFirst().orElse(i.stream().max(Comparator.comparingInt(DmIcon::getPriority)).orElse(null));
                    i.remove(def);

                    for (Map.Entry<String, Object> entry : def.toTrMenuSection().getValues(false).entrySet()) {
                        button.set(entry.getKey(), entry.getValue());
                    }
                    AtomicInteger priority = new AtomicInteger(1);
                    List<ConfigurationSection> priorityIcons = Lists.newArrayList();
                    i = i.stream().sorted(Comparator.comparingInt(DmIcon::getPriority)).collect(Collectors.toList());
                    Collections.reverse(i);
                    i.forEach(pI -> {
                        ConfigurationSection section = pI.toTrMenuSection(priority.get());
                        priorityIcons.add(section);
                        priority.getAndIncrement();
                    });
                    if (priorityIcons.size() > 0) {
                        button.set("icons", priorityIcons);
                    }
                }
                tm.set("Buttons." + key, button);
            });
            tm.set("Shape", shape);
            if (file != null) {
                file.renameTo(new File(file.getParent(), file.getName() + ".MIGRATED"));
            }
            tm.save(new File(CommandMigrate.getFolder(), menu));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void applySlots(List<String> shape, List<Integer> slots, char key) {
        slots.forEach(slot -> {
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
        });
    }

    private static List<String> getEmptyShape(int size) {
        List<String> shape = Lists.newArrayList();
        for (int i = 0; i < size / 9; i++) {
            shape.add("         ");
        }
        return shape;
    }

    private static class DmIcon {

        private int priority;
        private String requirement;
        private int update;
        private List<Integer> slots;
        private String material;
        private String name;
        private List<String> lore;
        private String amount;
        private String shiny;
        private List<String> flags;
        private HashMap<String, List<String>> actions;

        public DmIcon(int priority, String requirement, int update, List<Integer> slots, String material, String name, List<String> lore, String amount, String shiny, List<String> flags, HashMap<String, List<String>> actions) {
            this.priority = priority;
            this.requirement = requirement;
            this.update = update;
            this.slots = slots;
            this.material = material;
            this.name = name;
            this.lore = lore;
            this.amount = amount;
            this.shiny = shiny;
            this.flags = flags;
            this.actions = actions;
        }

        public ConfigurationSection toTrMenuSection(int priority) {
            MemorySection section = new MemoryConfiguration();
            if (getRequirement() != null) {
                section.set("condition", getRequirement());
            }
            if (priority >= 0) {
                section.set("priority", priority);
            }
            section.set("display.material", getMaterial());
            if (getName() != null) {
                section.set("display.name", getName());
            }
            if (getLore() != null) {
                section.set("display.lore", getLore());
            }
            if (getAmount() != null) {
                section.set("display.amount", getAmount());
            }
            if (getShiny() != null && !"false".equalsIgnoreCase(getShiny())) {
                if (Boolean.parseBoolean(getShiny())) {
                    section.set("display.shiny", true);
                } else {
                    section.set("display.shiny", getShiny());
                }
            }
            if (getFlags() != null && !getFlags().isEmpty()) {
                section.set("display.flags", getFlags());
            }
            actions.forEach((type, actions) -> section.set("actions." + type, actions));
            return section;
        }

        public ConfigurationSection toTrMenuSection() {
            return toTrMenuSection(-1);
        }

        public int getPriority() {
            return priority;
        }

        public String getRequirement() {
            return requirement;
        }

        public int getUpdate() {
            return update;
        }

        public List<Integer> getSlots() {
            return slots;
        }

        public String getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public List<String> getLore() {
            return lore;
        }

        public String getAmount() {
            return amount;
        }

        public String getShiny() {
            return shiny;
        }

        public List<String> getFlags() {
            return flags;
        }

        public HashMap<String, List<String>> getActions() {
            return actions;
        }
    }

    /*
    MIGRATE METHODS
     */

    private static HashMap<String, List<String>> migrateIconActions(ConfigurationSection icon) {
        HashMap<String, List<String>> actions = new HashMap<>();
        Arrays.asList("left", "right", "middle", "shift_left").forEach(type -> {
            if (icon.contains(type + "_click_commands")) {
                actions.put(type, migrateIconCommands(icon.getStringList(type + "_click_commands")));
                if (icon.contains(type + "_click_requirement")) {
                    String require = migrateRequires(icon.getConfigurationSection(type + "_click_requirement.requirements"));
                    List<String> requireActions = migrateIconCommands(icon.getStringList(type + "_click_requirement.deny_commands"));
                    for (int i = 0; i < requireActions.size(); i++) {
                        requireActions.set(i, requireActions.get(i) + "<require:" + require + ">");
                    }
                    requireActions.add("Break" + "<require:" + require + ">");
                    actions.get(type).addAll(0, requireActions);
                }
            }
        });
        if (actions.values().size() > 1) {
            List<String> def = new ArrayList<>(actions.values()).get(0);
            boolean allSame = true;
            for (List<String> value : actions.values()) {
                if (!def.equals(value)) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) {
                actions.clear();
                actions.put("all", def);
            }
        }
        return actions;
    }

    private static List<String> migrateFlags(ConfigurationSection icon) {
        List<String> flags = Lists.newArrayList();
        if (icon.getBoolean("hide_enchantments", false)) {
            flags.add("HIDE_ENCHANTS");
        }
        if (icon.getBoolean("hide_attributes", false)) {
            flags.add("HIDE_ATTRIBUTES");
        }
        if (icon.getBoolean("hide_effects", false)) {
            flags.add("HIDE_POTION_EFFECTS");
        }
        return flags;
    }

    private static String migrateMaterial(ConfigurationSection icon) {
        String material = icon.getString("material");
        if (icon.contains("data") && icon.getInt("data", 0) > 0) {
            material += "<data-value:" + icon.getInt("data") + ">";
        }
        if (icon.contains("banner_meta")) {
            StringBuilder pattern = new StringBuilder();
            icon.getStringList("banner_meta").forEach(b -> {
                pattern.append(b.replace(";", " ")).append(",");
            });
            material += "<banner:" + pattern.substring(0, pattern.length() - 1) + ">";
        }
        if (icon.contains("rgb")) {
            material += "<dye:" + icon.get("rgb") + ">";
        }
        if (material.startsWith("head;")) {
            material = "<head:" + material.substring(5) + ">";
        } else if (material.startsWith("basehead-")) {
            material = "<skull:" + material.substring(9) + ">";
        } else if (material.startsWith("heads-")) {
            material = "<hdb:" + material.substring(6) + ">";
        } else if (material.startsWith("hdb-")) {
            material = "<hdb:" + material.substring(4) + ">";
        } else if (material.startsWith("placeholder-")) {
            material = material.substring(12) + "<variable>";
        }
        return material;
    }

    private static List<String> migrateIconCommands(List<String> iconCommands) {
        for (int i = 0; i < iconCommands.size(); i++) {
            iconCommands.set(i, migrateIconCommand(iconCommands.get(i)));
        }
        return iconCommands;
    }

    private static String migrateIconCommand(String iconCommand) {
        return iconCommand
                .replace("[player]", "player:")
                .replace("[commandevent]", "player:")
                .replace("[console]", "console:")
                .replace("[message]", "tell:")
                .replace("[openguimenu]", "open:")
                .replace("[connect]", "connect:")
                .replace("[close]", "close")
                .replace("[json]", "console: tellraw %player_name%")
                .replace("[refresh]", "refresh")
                .replace("[broadcastsound]", "sound: <players>")
                .replace("[takemoney]", "take-money:")
                .replace("<delay=", "<delay:")
                .replace("<chance=", "<chance:")
                ;
    }

    private static String migrateRequires(ConfigurationSection requirements) {
        StringBuilder result = new StringBuilder();
        requirements.getKeys(false).forEach(r -> {
            Map<String, Object> require = requirements.getConfigurationSection(r).getValues(false);
            String type = String.valueOf(require.get("type"));
            if ("has permission".equalsIgnoreCase(type)) {
                result.append("player.hasPermission(\"").append(require.get("permission")).append("\")");
            } else if ("has money".equalsIgnoreCase(type)) {
                result.append("%vault_eco_balance% >= ").append(require.get("amount"));
            } else if ("has item".equalsIgnoreCase(type)) {
                String checkItem = "\"%checkitem_";
                if (require.containsKey("material")) {
                    checkItem += "mat:" + require.get("material") + ",";
                } else if (require.containsKey("data")) {
                    checkItem += "data:" + require.get("data") + ",";
                } else if (require.containsKey("amount")) {
                    checkItem += "amt:" + require.get("amount") + ",";
                } else if (require.containsKey("name")) {
                    checkItem += "nameequals:" + require.get("name") + ",";
                } else if (require.containsKey("lore")) {
                    checkItem += "lorecontains:" + listAsString(require.get("lore")) + ",";
                }
                result.append(checkItem).append("%\" == \"yes\"");
            } else if ("javascript".equalsIgnoreCase(type)) {
                result.append(require.get("expression"));
            } else if ("string equals".equalsIgnoreCase(type)) {
                result.append("\"").append(require.get("input")).append("\" == \"").append(require.get("output")).append("\"");
            } else if ("string equals ignorecase".equalsIgnoreCase(type)) {
                result.append("\"").append(require.get("input")).append("\".toLowerCase() == \"").append(require.get("output")).append("\".toLowerCase()");
            } else if ("string contains".equalsIgnoreCase(type)) {
                result.append("\"").append(require.get("input")).append("\".indexOf(\"").append(require.get("output")).append("\") >= 0");
            } else if (require.containsKey("input") && require.containsKey("output")) {
                result.append(require.get("input")).append(" ").append(type).append(" ").append(require.get("output"));
            }
            result.append(" && ");
        });
        return result.substring(0, result.length() - 4);
    }

    /*
    UTILS
     */

    private static String listAsString(Object object) {
        StringBuilder result = new StringBuilder();
        if (object instanceof List) {
            ((List) object).forEach(o -> result.append(o).append("\n"));
            result.substring(0, result.length() - 1);
        } else {
            result.append(object);
        }
        return result.toString();
    }

}
