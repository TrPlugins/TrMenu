package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Arasple
 * @date 2020/1/12 21:46
 */
public class CommandAbout extends BaseSubCommand {

    private static boolean loading;
    private static Menu aboutMenu;

    {
        loadMenu();
    }

    private void loadMenu() {
        if (!loading) {
            loading = true;
            Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                try (InputStream inputStream = new URL("https://raw.githubusercontent.com/Arasple/TrMenu/master/files/trmenu.yml").openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(bufferedInputStream)));
                    Menu.Load menu = MenuLoader.loadMenu(cfg.getValues(false), cfg.getName());
                    if (menu != null) {
                        aboutMenu = menu.getMenu();
                        aboutMenu.setOpenCommands(new ArrayList<>());
                    }
                } catch (Throwable ignored) {
                }
                loading = false;
            });
        }
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && "reload".equalsIgnoreCase(args[0])) {
            loadMenu();
            sender.sendMessage("§2Reloading the about menu...");
            return;
        }
        if (aboutMenu == null) {
            loadMenu();
            sender.sendMessage("§7Please wait... GUI is loading...");
        } else {
            aboutMenu.open((Player) sender);
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    public static Menu getAboutMenu() {
        return aboutMenu;
    }

}
