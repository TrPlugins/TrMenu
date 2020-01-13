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

/**
 * @author Arasple
 * @date 2020/1/12 21:46
 */
public class CommandAbout extends BaseSubCommand {

    private static Menu aboutMenu;

    {
        loadMenu();
    }

    private void loadMenu() {
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            try (InputStream inputStream = new URL("https://raw.githubusercontent.com/Arasple/TrMenu/master/trmenu.yml").openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(bufferedInputStream)));
                aboutMenu = MenuLoader.loadMenuFromYAML(configuration);
            } catch (Throwable ignored) {
            }
        });
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
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

}
