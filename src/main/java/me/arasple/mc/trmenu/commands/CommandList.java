package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2020/1/12 21:44
 */
public class CommandList extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Filter", false)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        String filter = args.length > 0 ? ArrayUtil.arrayJoin(args, 0).toLowerCase() : null;
        TLocale.sendTo(sender, "COMMANDS.LIST");
        TrMenuAPI.getMenuIds().stream().filter(m -> filter == null || m.toLowerCase().contains(filter)).forEach(id -> TellrawJson.create().append(Strings.replaceWithOrder(TLocale.asString("COMMANDS.LIST-FORMAT"), id + ".yml")).hoverText("§7点击立即打开!").clickCommand("/trmenu open " + id).send(sender));
        sender.sendMessage("");
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
