package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
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
        MenuLoader.loadMenus(sender);
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
