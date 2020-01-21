package me.arasple.mc.trmenu.commands;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.migrate.DeluxeMenusMigrater;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/21 19:55
 */
public class CommandMigrate extends BaseSubCommand {

    private File folder = new File("plugins/TrMenu/migrated");

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Type", true, () -> Collections.singletonList("DeluxeMenus")), new Argument("File/Dir name", true, this::getMigrateFiles)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!"DeluxeMenus".equalsIgnoreCase(args[0])) {
            TLocale.sendTo(sender, "MIGRATE.UNSUPPORTED-TYPE", args[0]);
            return;
        }
        File file = new File(TrMenu.getPlugin().getDataFolder(), args[1]);
        if (!file.exists() || countFiles(file) <= 0) {
            TLocale.sendTo(sender, "MIGRATE.NOT-EXISTED", args[1]);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                int c = countFiles(file);
                TLocale.sendTo(sender, "MIGRATE.PROCESSING", c);
                int r = migrateMenu(file);
                if (r < c) {
                    TLocale.sendTo(sender, "MIGRATE.ERROR", c - r);
                }
            });
        }
    }

    private int migrateMenu(File file) {
        int i = 0;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                i += migrateMenu(f);
            }
        } else {
            try {
                i++;
                DeluxeMenusMigrater.migrateDeluxeMenu(file).save(new File(folder, file.getName()));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    private int countFiles(File file) {
        int i = 0;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                i += countFiles(f);
            }
        } else {
            return i + (file.getName().toLowerCase().endsWith(".yml") ? 1 : 0);
        }
        return i;
    }

    private List<String> getMigrateFiles() {
        List<String> result = Lists.newArrayList();
        for (File file : TrMenu.getPlugin().getDataFolder().listFiles()) {
            if (!file.getName().matches("lang|menus|migrated|settings.yml")) {
                result.add(file.getName());
            }
        }
        return result;
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
