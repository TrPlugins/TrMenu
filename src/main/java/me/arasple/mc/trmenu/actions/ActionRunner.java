package me.arasple.mc.trmenu.actions;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ext.IconActionDealy;
import me.arasple.mc.trmenu.data.ArgsCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.*;

/**
 * @author Arasple
 * @date 2019/10/5 9:16
 */
public class ActionRunner {

    public static void runActions(List<BaseAction> orgActions, Player player, InventoryClickEvent e) {
        if (orgActions == null || orgActions.isEmpty()) {
            return;
        }

        HashMap<String, List<BaseAction>> actionGroups = new HashMap<>();

        orgActions.forEach(baseAction -> {
            actionGroups.putIfAbsent(baseAction.getRequirement(), new ArrayList<>());
            actionGroups.get(baseAction.getRequirement()).add(baseAction);
        });


        actionGroups.forEach((requirement, actions) -> {
            if (!isRequirementMatch(requirement, player, e)) {
                return;
            }

            executeActions(actions.listIterator(), player, e);
        });
    }

    private static void executeActions(ListIterator<BaseAction> actionListIterator, Player player, InventoryClickEvent e) {
        while (actionListIterator.hasNext()) {
            BaseAction action = actionListIterator.next();

            if (action instanceof IconActionDealy) {
                long delay = ((IconActionDealy) action).getDelay();
                if (delay > 0) {
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> executeActions(actionListIterator, player, e), delay);
                    break;
                }
            } else {
                action.onExecute(player, e, ArgsCache.getPlayerArgs(player));
            }
        }
    }

    private static boolean isRequirementMatch(String requirement, Player player, InventoryClickEvent event) {
        if (requirement != null && !Strings.isEmpty(requirement)) {
            try {
                Map<String, Object> bind = new HashMap<>();
                bind.put("player", player);
                if (event != null) {
                    bind.put("clickEvent", event);
                    if (event.getClick() != null) {
                        bind.put("clickType", event.getClick());
                    }
                }
                return (boolean) Scripts.compile(TLocale.Translate.setPlaceholders(player, requirement)).eval(new SimpleBindings(bind));
            } catch (ScriptException e) {
                TrMenu.getTLogger().error("&c条件运算时发生异常: &6{Requirement" + requirement + "}&8; &6Error: &4" + e.getMessage());
            }
        }
        return false;
    }

}
