package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/12 21:45
 */
public class CommandOpenMenu extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Menu name", true, TrMenuAPI::getMenuIds), new Argument("A player", false)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            TLocale.sendTo(sender, "COMMANDS.HELP-PAGE", TrMenu.getPlugin().getDescription().getVersion(), label.toUpperCase().charAt(0) + label.substring(1));
            return;
        }
        Menu menu = TrMenuAPI.getMenu(args[0]);
        if (menu == null) {
            TLocale.sendTo(sender, "MENU.NOT-EXIST");
            return;
        }
        if (args.length == 1) {
            if (sender instanceof Player) {
                menu.open((Player) sender);
            } else {
                TLocale.sendTo(sender, "COMMANDS.NOT-PLAYER");
            }
        }
        if (args.length >= 2) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null || !player.isOnline()) {
                TLocale.sendTo(sender, "COMMANDS.NO-PLAYER");
            } else {
                menu.open(player);
                if (!(args.length >= 3 && "silent".equalsIgnoreCase(args[2]))) {
                    TLocale.sendTo(sender, "COMMANDS.OPENED-FOR");
                }
            }
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
