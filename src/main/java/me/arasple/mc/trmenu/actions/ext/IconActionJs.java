package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;

import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionJs extends BaseAction {

    public IconActionJs(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {
        String js = TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(getCommand(), args));
        try {
            Map<String, Object> bind = new HashMap<>();
            bind.put("player", player);
            if (e instanceof InventoryClickEvent) {
                bind.put("clickEvent", e);
                if (((InventoryClickEvent) e).getClick() != null) {
                    bind.put("clickType", ((InventoryClickEvent) e).getClick());
                }
            }
            Scripts.compile(js).eval(new SimpleBindings(bind));
        } catch (ScriptException ex) {
            player.sendMessage("§c执行Js脚本发生异常: §8{JS:" + js + "}, §4" + ex.getMessage());
            player.sendMessage("§8" + Arrays.toString(ex.getStackTrace()));
        }
    }

}
