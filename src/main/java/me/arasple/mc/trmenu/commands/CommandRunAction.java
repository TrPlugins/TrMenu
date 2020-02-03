package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trmenu.utils.TrUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/2/3 11:13
 */
public class CommandRunAction extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Player", true), new Argument("Action Line", true)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null || !player.isOnline()) {
            TLocale.sendTo(sender, "COMMANDS.NO-PLAYER");
            return;
        }
        String action = ArrayUtil.arrayJoin(args, 1);
        TrUtils.getInst().runAction(player, action);
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
