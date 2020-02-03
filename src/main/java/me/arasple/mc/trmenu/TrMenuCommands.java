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
@BaseCommand(name = "trmenu", aliases = {"menu", "tmenu"}, permission = "trmenu.use")
public class TrMenuCommands extends BaseMainCommand {

    @SubCommand(description = "Reload menus", permission = "trmenu.command.reload")
    BaseSubCommand reload = new CommandReload();

    @SubCommand(description = "List menus", permission = "trmenu.command.list")
    BaseSubCommand list = new CommandList();

    @SubCommand(description = "Open a menu for a player", permission = "trmenu.command.open")
    BaseSubCommand open = new CommandOpenMenu();

    @SubCommand(description = "Write item to JSON", permission = "trmenu.command.item")
    BaseSubCommand item = new CommandItemToJson();

    @SubCommand(description = "Run action", permission = "trmenu.command.runaction")
    BaseSubCommand runAction = new CommandRunAction();

    @SubCommand(description = "Template manage", permission = "trmenu.command.template")
    BaseSubCommand template = new CommandTemplate();

    @SubCommand(description = "Paste your menu on hastebin", permission = "trmenu.command.share")
    BaseSubCommand share = new CommandShare();

    @SubCommand(description = "Migrate menus from other plugins", permission = "trmenu.command.migrate")
    BaseSubCommand migrate = new CommandMigrate();

    @SubCommand(description = "Toggle the Debug Mode", permission = "trmenu.command.debug")
    BaseSubCommand debug = new CommandDebug();

    @SubCommand(description = "Update the plugin", permission = "trmenu.command.update")
    BaseSubCommand update = new CommandUpdate();

    @SubCommand(description = "About this plugin", permission = "trmenu.command.about")
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
