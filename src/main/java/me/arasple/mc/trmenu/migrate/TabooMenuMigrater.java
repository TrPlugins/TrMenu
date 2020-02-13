package me.arasple.mc.trmenu.migrate;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.commands.CommandMigrate;
import me.arasple.mc.trmenu.hook.HookHeadDatabase;
import me.arasple.mc.trmenu.utils.Skulls;
import me.skymc.taboomenu.TabooMenu;
import me.skymc.taboomenu.display.Icon;
import me.skymc.taboomenu.display.data.IconCommand;
import me.skymc.taboomenu.display.data.Requirement;
import me.skymc.taboomenu.iconcommand.AbstractIconCommand;
import me.skymc.taboomenu.iconcommand.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Arasple
 * @date 2020/2/10 17:42
 * 有损迁移 TabooMenu 菜单
 */
public class TabooMenuMigrater {

    public static int migrateTabooMenu() {
        if (Bukkit.getPluginManager().getPlugin("TabooMenu") == null || !Bukkit.getPluginManager().getPlugin("TabooMenu").isEnabled()) {
            return 0;
        }

        AtomicInteger count = new AtomicInteger();
        TabooMenu.getMenus().forEach(menu -> {
            YamlConfiguration trmenu = new YamlConfiguration();
            trmenu.options().header(
                    " " + "\n" +
                            "Migrated from TabooMenu, by TrMenu" + "\n" +
                            "Date: " + new Date().toString() + "\n" +
                            " " + "\n"
            );
            trmenu.set("Title", menu.getName());
            trmenu.set("Rows", menu.getRows());
            trmenu.set("Open-Commands", menu.getOpenCommand());

            List<String> openActions = migrateIconCommands(menu.getOpenAction()), closeActions = migrateIconCommands(menu.getCloseAction());
            if (!openActions.isEmpty()) {
                trmenu.set("Open-Actions", openActions);
            }
            if (!closeActions.isEmpty()) {
                trmenu.set("Close-Actions", closeActions);
            }

            if (Strings.nonEmpty(menu.getPrevious())) {
                if (!closeActions.isEmpty()) {
                    closeActions.add("open: " + menu.getPrevious());
                    trmenu.set("Close-Actions", closeActions);
                }
            }
            if (Strings.nonEmpty(menu.getPermission()) && !menu.isPermissionBypass()) {
                trmenu.set("Open-Requirement", "player.hasPermission(\"" + menu.getPermission() + "\")");
            }
            int update = menu.getAutoRefresh();
            trmenu.set("Buttons", migrateButtons(menu.getIcons()));
            try {
                trmenu.save(new File(CommandMigrate.getFolder(), menu.getFile().getName()));
                count.getAndIncrement();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return count.get();
    }

    private static ConfigurationSection migrateButtons(Map<Integer, Icon> value) {
        ConfigurationSection buttons = new MemoryConfiguration();
        value.forEach((slot, button) -> {
            ConfigurationSection b = new MemoryConfiguration();
            ConfigurationSection defIcon = migrateIcon(button, slot);

            b.set("display", defIcon.getConfigurationSection("display"));
            if (defIcon.isSet("actions")) {
                b.set("actions", defIcon.getConfigurationSection("actions"));
            }
            List<ConfigurationSection> priorityIcons = new ArrayList<>();

            List<Requirement> requirements = button.getRequirements();
            requirements.sort(Collections.reverseOrder(Comparator.comparingInt(Requirement::getPriority)));
            AtomicInteger priority = new AtomicInteger(1);
            requirements.forEach(requirement -> {
                ConfigurationSection pIcon = new MemoryConfiguration();
                ConfigurationSection migrated = migrateIcon(requirement.getIcon(), -1);
                pIcon.set("condition", requirement.getExpression());
                pIcon.set("priority", priority.getAndIncrement());
                pIcon.set("display", migrated.getConfigurationSection("display"));
                if (migrated.contains("actions")) {
                    pIcon.set("actions", migrated.getConfigurationSection("actions"));
                }
                priorityIcons.add(pIcon);
            });
            if (!priorityIcons.isEmpty()) {
                b.set("icons", priorityIcons);
            }
            buttons.set(button.getIconName(), b);
        });
        return buttons;
    }

    private static ConfigurationSection migrateIcon(Icon button, int slot) {
        ConfigurationSection icons = new MemoryConfiguration();

        icons.set("display.mats", migrateMat(button));
        if (slot > 0) {
            icons.set("display.slots", migrateSlots(slot, button));
        }
        if (Strings.nonEmpty(button.getName())) {
            icons.set("display.name", button.getName());
        }
        if (button.getLore() != null) {
            icons.set("display.lore", button.getLore());
        }
        if (button.getAmount() != 1) {
            icons.set("display.amount", button.getAmount());
        }
        if (button.isShiny()) {
            icons.set("display.shiny", true);
        }
        if (!button.isHideAttribute()) {
            icons.set("display.flags", Collections.singletonList("HIDE_ATTRIBUTES"));
        }
        migrateIconActions(button.getIconCommands()).forEach((type, actions) -> {
            if (!actions.isEmpty()) {
                icons.set("actions." + type, actions);
            }
        });
        return icons;
    }

    private static Map<String, List<String>> migrateIconActions(List<IconCommand> iconCommands) {
        Map<String, List<String>> actions = new HashMap<>();
        iconCommands.forEach(iconCommand -> {
            iconCommand.getClickType().forEach(n -> {
                String type = n.name().toLowerCase();
                actions.putIfAbsent(type, new ArrayList<>());
                actions.get(type).addAll(migrateIconCommands(iconCommand.getCommands()));
            });
        });
        return actions;
    }

    private static Object migrateSlots(int slot, Icon button) {
        List<Integer> slots = new ArrayList<>();
        if (!button.isFull()) {
            slots.add(slot);
            button.getSlotCopy().forEach(s -> {
                if (!slots.contains(s)) {
                    slots.add(s);
                }
            });
        } else {
            TabooMenu.getMenus().stream().filter(menu -> menu.getFile().getName().equals(button.getMenuName())).findFirst().ifPresent(menu -> {
                menu.getIcons().forEach((slotInt, icon) -> {
                    if (!slots.contains(slotInt)) {
                        slots.add(slotInt);
                    }
                    icon.getSlotCopy().forEach(s -> {
                        if (!slots.contains(s)) {
                            slots.add(s);
                        }
                    });
                });
            });
        }

        return slots.size() > 1 ? slots : slots.get(0);
    }

    private static String migrateMat(Icon icon) {
        if (icon.getItemSource() != null) {
            ItemStack itemStack = icon.getItemSource();
            if (itemStack.getItemMeta() instanceof SkullMeta) {
                String hdb = HookHeadDatabase.getId(itemStack);
                return Strings.nonEmpty(hdb) ? "<hdb:" + hdb + ">" : "<skull:" + Skulls.getTexture(itemStack) + ">";
            }
        }
        if (Strings.nonEmpty(icon.getSkullTexture())) {
            return "<skull:" + icon.getSkullTexture() + ">";
        } else if (Strings.nonEmpty(icon.getSkullOwner())) {
            return "<head:" + icon.getSkullOwner() + ">";
        } else if (icon.getColor() != null && !(icon.getColor().getRed() == 0 && icon.getColor().getGreen() == 0 && icon.getColor().getBlue() == 0)) {
            return icon.getMaterial().name().toLowerCase() + "<dye:" + icon.getColor().getRed() + "," + icon.getColor().getGreen() + "," + icon.getColor().getBlue() + ">";
        } else if (icon.getBannerPatterns() != null) {
            return icon.getMaterial().name().toLowerCase() + "<banner:" + String.join(",", icon.getBannerPatterns()) + ">";
        } else if (icon.getData() > 0) {
            return icon.getMaterial().name().toLowerCase() + ":" + icon.getData();
        }
        return icon.getMaterial().name().toLowerCase();
    }

    private static List<String> migrateIconCommands(List<AbstractIconCommand> iconCommands) {
        List<String> actions = Lists.newArrayList();
        iconCommands.forEach(command -> {
            if (command instanceof IconCommandBroadcast) {
                actions.add("tell: " + command.getCommand() + "<players>");
            } else if (command instanceof IconCommandClose) {
                actions.add("close");
            } else if (command instanceof IconCommandConsole) {
                actions.add("console: " + command.getCommand());
            } else if (command instanceof IconCommandDelay) {
                actions.add("delay: " + ((IconCommandDelay) command).getDelay());
            } else if (command instanceof IconCommandGiveItem) {
                // UNSUPPORTED
                actions.add("console: give %player_name% " + command.getCommand() + " [UNSUPPORTED-MIGRATE]");
            } else if (command instanceof IconCommandGiveMoney) {
                actions.add("give-money: " + ((IconCommandGiveMoney) command).getAmount());
            } else if (command instanceof IconCommandGivePoints) {
                actions.add("console: playerpoints give %player_name% " + ((IconCommandGivePoints) command).getAmount());
            } else if (command instanceof IconCommandMessage) {
                actions.add("tell: " + command.getCommand());
            } else if (command instanceof IconCommandOp) {
                actions.add("op: " + command.getCommand());
            } else if (command instanceof IconCommandOpen) {
                actions.add("open: " + command.getCommand());
            } else if (command instanceof IconCommandOpenForce) {
                actions.add("open: " + command.getCommand());
            } else if (command instanceof IconCommandPrompterConsole || command instanceof IconCommandPrompterOp || command instanceof IconCommandPrompterPlayer) {
                // UNSUPPORTED
                actions.add("catcher: [UNSUPPORTED-MIGRATE] " + command.getCommand());
            } else if (command instanceof IconCommandServer) {
                actions.add("server: " + command.getCommand());
            } else if (command instanceof IconCommandSound) {
                actions.add("sound: " + command.getCommand());
            } else if (command instanceof IconCommandTakeItems) {
                actions.add("take-item: " + command.getCommand());
            } else if (command instanceof IconCommandTakeMoney) {
                actions.add("take-money: " + ((IconCommandTakeMoney) command).getAmount());
            } else if (command instanceof IconCommandTakePoints) {
                actions.add("console: playerpoints take %player_name% " + ((IconCommandTakePoints) command).getAmount());
            } else if (command instanceof IconCommnadSoundBroadcast) {
                actions.add("sound: " + command.getCommand() + "<players>");
            } else {
                actions.add("command: " + command.getCommand());
            }
        });
        return actions;
    }

}