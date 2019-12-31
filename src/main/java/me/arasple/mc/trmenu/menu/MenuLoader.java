package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.utils.Notifys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/12/8 9:58
 */
public class MenuLoader {

    private static File folder;

    public static void init() {
        folder = new File(TrMenu.getPlugin().getDataFolder(), "menus");
        if (!folder.exists()) {
            TrMenu.getPlugin().saveResource("menus/example.yml", true);
        }
    }

    public static void loadFolderMenus(CommandSender... receivers) {
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            long start = System.currentTimeMillis();
            int all = MenuLoader.getMenuFilesCount(folder);
            List<String> errors = MenuLoader.loadMenu(folder);

            if (TrMenu.getSettings().isSet("MENU-FILES")) {
                for (String path : TrMenu.getSettings().getStringList("MENU-FILES")) {
                    File menuFile = new File(path);
                    if (menuFile.exists() && menuFile.getName().toLowerCase().endsWith(".yml")) {
                        errors.addAll(MenuLoader.loadMenu(menuFile));
                    }
                }
            }

            int loaded = TrMenuAPI.getMenus().size();
            if (loaded > 0) {
                Notifys.notify(receivers, "MENU.LOADED-SUCCESS", loaded, System.currentTimeMillis() - start);
            }

            if (all - loaded > 0 && !errors.isEmpty() && errors.size() > 0) {
                Notifys.notify(receivers, "MENU.LOADED-FAILURE", all - loaded);
                Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
                errors.forEach(error -> Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §bINFO §8| " + error));
                Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
            }
        });
    }

    public static List<String> loadMenu(File menu) {
        return loadMenu(menu, false);
    }

    public static List<String> loadMenu(File menu, boolean replaceOld) {
        return null;
    }

    /**
     * 取得一个图标在形状中的位置
     *
     * @param type  容器类型
     * @param shape 布局
     * @param key   按钮字符
     * @return 槽位
     */
    private static List<Integer> locateButton(List<String> shape, InventoryType type, char key) {
        shape = fixShape(shape);
        List<Integer> slots = Lists.newArrayList();
        for (int line = 1; line <= shape.size(); line++) {
            String l = shape.get(line - 1);
            for (int index = 1; index <= l.toCharArray().length; index++) {
                if (key == l.charAt(index - 1)) {
                    int slot = 9 * (line - 1) + index - 1;
                    slots.add(slot);
                }
            }
        }
        return slots;
    }

    private static List<String> fixShape(List<String> shape) {
        if (shape == null) {
            return shape;
        }
        while (shape.size() > 6) {
            shape.remove(shape.size() - 1);
        }
        for (int i = 0; i < shape.size(); i++) {
            if (shape.get(i).length() > 9) {
                shape.set(i, shape.get(i).substring(0, 9));
            }
        }
        return shape;
    }

    public static int getMenuFilesCount(File folder) {
        int count = 0;
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                count += getMenuFilesCount(file);
            }
        } else {
            count += folder.getName().toLowerCase().endsWith(".yml") ? 1 : 0;
        }
        return count;
    }

}
