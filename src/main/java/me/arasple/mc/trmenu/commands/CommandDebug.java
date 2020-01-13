package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.utils.Skulls;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author Arasple
 * @date 2020/1/12 21:46
 */
public class CommandDebug extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasMetadata("TrMenu-Debug")) {
                player.removeMetadata("TrMenu-Debug", TrMenu.getPlugin());
                sender.sendMessage("§7Canceled...");
            } else {
                player.setMetadata("TrMenu-Debug", new FixedMetadataValue(TrMenu.getPlugin(), ""));
                sender.sendMessage("§aEnabled...");
            }
            return;
        }

        sender.sendMessage("§3--------------------------------------------------");
        sender.sendMessage("");
        sender.sendMessage("§2Total Menus: §6" + TrMenuAPI.getMenus().size());
        sender.sendMessage("§2Cached Skulls: §6" + Skulls.getSkulls().size());
        sender.sendMessage("§2Running Tasks: §6" + Bukkit.getScheduler().getActiveWorkers().stream().filter(t -> t.getOwner() == TrMenu.getPlugin()).count() + Bukkit.getScheduler().getPendingTasks().stream().filter(t -> t.getOwner() == TrMenu.getPlugin()).count());
        sender.sendMessage("§2Metrics: §6" + Metrics.getMetrics().isEnabled() + " §7/ §8" + Metrics.getMetrics().getPluginData());
        sender.sendMessage("");
        sender.sendMessage("§3--------------------------------------------------");
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
