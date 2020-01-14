package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocaleLoader;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.menu.MenuLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2020/1/12 21:39
 */
public class CommandReload extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (!TrMenu.getSettings().getStringList("LOCALE-PRIORITY").equals(TLocaleLoader.getLocalePriority(TrMenu.getPlugin()))) {
                TLocaleLoader.setLocalePriority(TrMenu.getPlugin(), TrMenu.getSettings().getStringList("LOCALE-PRIORITY"));
                TLocaleLoader.load(TrMenu.getPlugin(), true, true);
            }
        } catch (Throwable ignored) {
        }
        MenuLoader.loadMenus(sender);
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
