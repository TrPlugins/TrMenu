package me.arasple.mc.trmenu;

import io.izzel.taboolib.module.command.base.*;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.inv.Menur;
import me.arasple.mc.trmenu.loader.MenuLoader;
import me.arasple.mc.trmenu.utils.Skulls;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/10/3 16:32
 */
@BaseCommand(name = "trmenu", aliases = {"menu", "tmenu"}, permission = "trmenu.admin")
public class TrMenuCommands extends BaseMainCommand {

    /**
     * 重载命令
     */
    @SubCommand(priority = 2)
    BaseSubCommand reload = new BaseSubCommand() {

        @Override
        public void onCommand(CommandSender sender, Command command, String label, String[] args) {
            MenuLoader.loadMenus(TrMenu.getMenus(), sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.ALL;
        }

    };
    /**
     * 列出所有菜单命令
     */
    @SubCommand(priority = 3)
    BaseSubCommand list = new BaseSubCommand() {

        @Override
        public void onCommand(CommandSender sender, Command command, String label, String[] args) {
            TLocale.sendTo(sender, "COMMANDS.LIST");
            TrMenuAPI.getMenuIds().forEach(id -> TellrawJson.create().append(Strings.replaceWithOrder(TLocale.asString("COMMANDS.LIST-FORMAT"), id + ".yml")).hoverText("§7点击立即打开!").clickCommand("/trmenu open " + id).send(sender));
            sender.sendMessage("");
        }

        @Override
        public CommandType getType() {
            return CommandType.ALL;
        }

    };
    /**
     * (为指定玩家) 打开指定菜单命令
     */
    @SubCommand(priority = 4)
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{new Argument("菜单", true, TrMenuAPI::getMenuIds), new Argument("指定玩家", false)};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 1) {
                displayHelp(sender, label);
                return;
            }
            // tmenu open args
            Menur menu = TrMenuAPI.getMenu(args[0]);
            if (menu == null) {
                TLocale.sendTo(sender, "MENU.NOT-EXIST");
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
                    TLocale.sendTo(sender, "COMMANDS.OPENED-FOR");
                }
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.ALL;
        }

    };
    @SubCommand(priority = 1)
    BaseSubCommand debug = new BaseSubCommand() {

        @Override
        public void onCommand(CommandSender sender, Command command, String label, String[] args) {
            sender.sendMessage("§3--------------------------------------------------");
            sender.sendMessage("");
            sender.sendMessage("§2Total Menus: §6" + TrMenuAPI.getMenus().size());
            sender.sendMessage("§2Cached Skulls: §6" + Skulls.getSkulls().size());
            sender.sendMessage("§2Running Tasks: §6" + Bukkit.getScheduler().getActiveWorkers().size() + Bukkit.getScheduler().getPendingTasks().size());
            sender.sendMessage("§2Metrics: §6" + Metrics.getMetrics().isEnabled() + " §7/ §8" + Metrics.getMetrics().getPluginData());
            sender.sendMessage("");
            sender.sendMessage("§3--------------------------------------------------");
        }

        @Override
        public CommandType getType() {
            return CommandType.ALL;
        }
    };

    @Override
    public String getCommandTitle() {
        return "§3--------------------------------------------------";
    }

    @Override
    public void onCommandHelp(CommandSender sender, Command command, String label, String[] args) {
        displayHelp(sender, label);
    }

    private void displayHelp(CommandSender sender, String label) {
        TLocale.sendTo(sender, "COMMANDS.HELP-PAGE", TrMenu.getPlugin().getDescription().getVersion(), label.toUpperCase().charAt(0) + label.substring(1));
    }

}
