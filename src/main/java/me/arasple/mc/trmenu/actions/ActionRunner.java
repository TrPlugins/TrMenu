package me.arasple.mc.trmenu.actions;

import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ext.IconActionBreak;
import me.arasple.mc.trmenu.actions.ext.IconActionDealy;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

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
            // 判断条件是否满足
            if (!isRequirementMatch(requirement, player, e)) {
                return;
            }

            executeActions(actions.listIterator(), player, e);
        });
    }

    private static void executeActions(ListIterator<BaseAction> actionListIterator, Player player, InventoryClickEvent e) {
        while (actionListIterator.hasNext()) {
            BaseAction action = actionListIterator.next();

            // 判断概率是否满足
            if (new Random().nextDouble() > action.getChance()) {
                continue;
            }

            // 延时执行
            if (action instanceof IconActionDealy) {
                long delay = ((IconActionDealy) action).getDelay();
                if (delay > 0) {
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> executeActions(actionListIterator, player, e), delay);
                    break;
                }

            }
            // 中断执行
            else if (action instanceof IconActionBreak) {
                break;
            } else {
                action.onExecute(player, e, ArgsCache.getPlayerArgs(player));
            }
        }

        actionListIterator.remove();
    }

    private static boolean isRequirementMatch(String requirement, Player player, InventoryClickEvent event) {
        if (requirement != null && !Strings.isEmpty(requirement)) {
            return (boolean) JavaScript.run(player, requirement, event, ArgsCache.getPlayerArgs(player));
        }
        return true;
    }

}
