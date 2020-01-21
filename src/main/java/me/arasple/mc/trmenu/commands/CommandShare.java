package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.utils.Hastebin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Arasple
 * @date 2020/1/21 10:37
 */
public class CommandShare extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || TrMenuAPI.getMenu(args[0]) == null) {
            TLocale.sendTo(sender, "MENU.NOT-EXIST");
        } else {
            Menu menu = TrMenuAPI.getMenu(args[0]);
            String loadedPath = menu.getLoadedPath();
            if (loadedPath == null) {
                TLocale.sendTo(sender, "MENU.NOT-SUPPORTED", menu.getName());
            } else {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new File(loadedPath));
                TLocale.sendTo(sender, "HASTEBIN.PROCESSING");
                Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                    String url = Hastebin.paste(yaml.saveToString());
                    TLocale.sendTo(sender, url != null ? "HASTEBIN.SUCCESS" : "HASTEBIN.FAILED", url);
                });
            }
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
