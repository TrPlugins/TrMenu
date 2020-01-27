package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.bstats.MetricsHandler;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.utils.Hastebin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Arasple
 * @date 2020/1/21 10:37
 */
public class CommandShare extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Menu name", true, TrMenuAPI::getMenuIds)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Menu menu = TrMenuAPI.getMenu(args[0]);
        if (menu == null) {
            TLocale.sendTo(sender, "MENU.NOT-EXIST");
            return;
        }
        String loadedPath = menu.getLoadedPath();
        if (loadedPath == null) {
            TLocale.sendTo(sender, "MENU.NOT-SUPPORTED", menu.getName());
        } else {
            MetricsHandler.increase(2);
            TLocale.sendTo(sender, "HASTEBIN.PROCESSING");
            Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                String url = Hastebin.paste(readMenu(loadedPath));
                TLocale.sendTo(sender, url != null ? "HASTEBIN.SUCCESS" : "HASTEBIN.FAILED", url);
            });
        }
    }

    private String readMenu(String file) {
        StringBuilder read = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String s;
            while ((s = r.readLine()) != null) {
                read.append(s).append("\n");
            }
        } catch (Throwable ignored) {
            return null;
        }
        return read.toString();
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
