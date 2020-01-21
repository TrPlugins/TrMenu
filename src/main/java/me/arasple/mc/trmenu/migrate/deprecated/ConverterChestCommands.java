package me.arasple.mc.trmenu.migrate.deprecated;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.menu.MenuLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Arasple
 * @date 2020/1/1 11:34
 */
@Deprecated
public class ConverterChestCommands {

    @TSchedule
    static void init() {
        File folder = new File("plugins/ChestCommands/menu");
        if (Bukkit.getPluginManager().getPlugin("ChestCommands") != null || folder.exists()) {
            if (folder.exists()) {
                int count = convert(folder, 0);
                if (count > 0) {
                    TLocale.sendToConsole("CONVERTER.CHESTCOMMANDS", count);
                    MenuLoader.loadMenus(Bukkit.getConsoleSender());
                }
            }
        }
    }

    private static int convert(File file, int count) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                count += convert(f, count);
            }
            return count;
        } else if (!file.getName().endsWith(".yml")) {
            return count;
        }
        try {
            ListIterator<Character> buttons = Arrays.asList('#', '-', '+', '=', '<', '>', '~', '_', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z').listIterator();
            YamlConfiguration trmenu = new YamlConfiguration();
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
            List<String> cmds = conf.contains("menu-settings.command") ? Arrays.asList(conf.getString("menu-settings.command").split(";( )?")) : new ArrayList<>();
            for (int i = 0; i < cmds.size(); i++) {
                cmds.set(i, cmds.get(i) + "-fromCC");
            }
            int rows = conf.getInt("menu-settings.rows", 6);
            int update = conf.getInt("menu-settings.auto-refresh", -1) * 20;
            trmenu.set("Title", conf.getString("menu-settings.name"));
            trmenu.set("Open-Commands", cmds);
            trmenu.set("Open-Actions", conf.contains("menu-settings.open-action") ? conf.getString("menu-settings.open-action").split(";( )?") : "");

            List<String> shape = Lists.newArrayList();
            while (rows > 0) {
                shape.add("         ");
                rows--;
            }
            trmenu.set("Shape", shape);

            conf.getValues(false).forEach((icon, value) -> {
                if (!"menu-settings".equalsIgnoreCase(icon)) {
                    MemorySection section = (MemorySection) value;
                    int x = section.getInt("POSITION-X") - 1;
                    int y = section.getInt("POSITION-Y") - 1;
                    char[] chars = shape.get(y).toCharArray();
                    char symbol = buttons.next();
                    chars[x] = symbol;
                    shape.set(y, new String(chars));

                    if (update > 0) {
                        trmenu.set("Buttons." + symbol + ".update", update);
                    }
                    trmenu.set("Buttons." + symbol + ".display.mats", section.get("ID"));
                    trmenu.set("Buttons." + symbol + ".display.name", section.get("NAME"));
                    trmenu.set("Buttons." + symbol + ".display.lore", section.get("LORE"));
                    if (section.contains("COMMAND")) {
                        trmenu.set("Buttons." + symbol + ".actions.all", section.getString("COMMAND").split(";( )?"));
                    }
                    if (section.contains("ENCHANTMENT")) {
                        trmenu.set("Buttons." + symbol + ".display.shiny", true);
                    }
                }
            });
            trmenu.set("Shape", fixShape(shape));
            file.renameTo(new File(file.getParent(), file.getName().replace(".yml", "") + ".CONVERTED.TRMENU"));
            trmenu.save(new File(MenuLoader.getFolder(), file.getName().replace(".yml", "") + "-fromcc.yml"));
            return count + 1;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return count;
    }

    private static List<String> fixShape(List<String> shape) {
        for (int i = 0; i < shape.size(); i++) {
            if (shape.get(i).length() > 9) {
                shape.set(i, shape.get(i).substring(0, 9));
            }
        }
        return shape;
    }

}