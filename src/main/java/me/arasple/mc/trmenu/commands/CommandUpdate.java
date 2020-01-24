package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.updater.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Arasple
 * @date 2020/1/12 21:46
 */
public class CommandUpdate extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Is Confirmed", false, () -> Arrays.asList("confirmed", "force"))};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            TLocale.sendToConsole("PLUGIN.UPDATER.CONFIRM");
        } else if ("Confirmed".equalsIgnoreCase(args[0])) {
            if (Updater.isOld()) {
                update();
            } else {
                TLocale.sendToConsole("PLUGIN.UPDATER." + (Updater.getVersion() > Updater.getNewVersion() ? "DEV" : "LATEST"));
            }
        } else if ("Force".equalsIgnoreCase(args[0])) {
            update();
        }
    }

    private void update() {
        String url = "https://arasple.oss-cn-beijing.aliyuncs.com/files/TrMenu.jar";
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOADING", Updater.getNewVersion());
            if (Files.downloadFile(url, TrMenu.getPlugin().getPluginFile())) {
                TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOAD-COMPLETED");
                return;
            }
            TLocale.sendToConsole("PLUGIN.UPDATER.DOWNLOAD-FAILED");
        });
    }

    @Override
    public CommandType getType() {
        return CommandType.CONSOLE;
    }

}
