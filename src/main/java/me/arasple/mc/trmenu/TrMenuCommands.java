package me.arasple.mc.trmenu;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2019/10/3 16:32
 */
@BaseCommand(name = "trmenu", aliases = {"menu", "tmenu"}, permission = "trmenu.admin")
public class TrMenuCommands extends BaseMainCommand {

    @SubCommand(description = "Reload menus")
    BaseSubCommand reload = new CommandReload();

    @SubCommand(description = "List menus")
    BaseSubCommand list = new CommandList();

    @SubCommand(description = "Open a menu for a player")
    BaseSubCommand open = new CommandOpenMenu();

    @SubCommand(description = "Write item to JSON", aliases = "itj")
    BaseSubCommand item = new CommandItemToJson();

    @SubCommand(description = "Template manage")
    BaseSubCommand template = new CommandTemplate();

    @SubCommand(description = "Toggle the Debug Mode")
    BaseSubCommand debug = new CommandDebug();

    @SubCommand(description = "About this plugin")
    BaseSubCommand about = new CommandAbout();

    @Override
    public String getCommandTitle() {
        return "§3--------------------------------------------------";
    }

    @Override
    public void onCommandHelp(CommandSender sender, Command command, String label, String[] args) {
        TLocale.sendTo(sender, "COMMANDS.HELP-PAGE", TrMenu.getPlugin().getDescription().getVersion(), label.toUpperCase().charAt(0) + label.substring(1));
    }

}
