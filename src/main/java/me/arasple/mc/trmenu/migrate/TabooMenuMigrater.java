package me.arasple.mc.trmenu.migrate;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import me.skymc.taboomenu.TabooMenu;
import me.skymc.taboomenu.display.Icon;
import me.skymc.taboomenu.iconcommand.AbstractIconCommand;
import me.skymc.taboomenu.iconcommand.impl.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/2/10 17:42
 */
public class TabooMenuMigrater {

    public static void migrateTabooMenu() {
        TabooMenu.getMenus().forEach(menu -> {
            YamlConfiguration trmenu = new YamlConfiguration();
            trmenu.options().header(
                    " " + "\n" +
                            "Migrated from TabooMenu, by TrMenu" + "\n" +
                            "Date: " + new Date().toString() + "\n" +
                            "Mr.Bkm NB!!!!" + "\n" +
                            " " + "\n"
            );
            trmenu.set("Title", menu.getName());
            trmenu.set("Rows", menu.getRows());
            trmenu.set("Open-Commands", menu.getOpenCommand());
            trmenu.set("Open-Actions", migrateIconCommands(menu.getOpenAction()));
            trmenu.set("Close-Actions", migrateIconCommands(menu.getCloseAction()));

            if (Strings.nonEmpty(menu.getPrevious())) {
                List<String> closeActions = trmenu.getStringList("Close-Actions");
                if (!closeActions.isEmpty()) {
                    closeActions.add("open: " + menu.getPrevious());
                    trmenu.set("Close-Actions", closeActions);
                }
            }
            if (Strings.nonEmpty(menu.getPermission())) {
                trmenu.set("Open-Requirement", "'player.hasPermission(\"" + menu.getPermission() + "\")'" + (menu.isPermissionBypass() ? " && !player.isOp()" : ""));
            }
            int update = menu.getAutoRefresh();
        });
    }

    private static ConfigurationSection migrateIcons(List<Icon> value) {
        ConfigurationSection icons = new MemoryConfiguration();

        return null;
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
                actions.add("console: playerpoints add %player_name% " + ((IconCommandGivePoints) command).getAmount());
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
            }
        });
        return actions;
    }

}