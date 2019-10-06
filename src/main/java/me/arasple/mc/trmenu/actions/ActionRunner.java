package me.arasple.mc.trmenu.actions;

import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ext.IconActionBreak;
import me.arasple.mc.trmenu.actions.ext.IconActionDealy;
import me.arasple.mc.trmenu.bstats.Metrics;
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

    /**
     * 将动作集合根据条件表达式分组并依次执行, 提高效率
     *
     * @param orgActions 原动作组
     * @param player     需要跑的玩家
     * @param e          容器事件
     */
    public static void runActions(List<BaseAction> orgActions, Player player, InventoryClickEvent e) {
        runActions(orgActions, player, e, null);
    }

    public static void runActions(List<BaseAction> orgActions, Player player, InventoryClickEvent e, Map<String, String> replace) {
        if (orgActions == null || orgActions.isEmpty()) {
            return;
        }
        Metrics.increase(3, orgActions.size());

        HashMap<String, List<BaseAction>> actionGroups = new HashMap<>();
        orgActions.forEach(baseAction -> {
            actionGroups.putIfAbsent(baseAction.getRequirement(), new ArrayList<>());
            actionGroups.get(baseAction.getRequirement()).add(baseAction);
        });

        actionGroups.forEach((requirement, actions) -> {
            // 判断条件是否满足
            if (!isRequirementMatch(requirement, player, e)) {
                orgActions.removeAll(actions);
            }
        });
        executeActions(orgActions.listIterator(), player, e, replace);
    }

    /**
     * 执行一组动作
     *
     * @param actionListIterator 动作
     * @param player             玩家
     * @param e                  容器事件
     */
    private static void executeActions(ListIterator<BaseAction> actionListIterator, Player player, InventoryClickEvent e, Map<String, String> replace) {
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
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> executeActions(actionListIterator, player, e, replace), delay);
                    break;
                }
            }
            // 中断执行
            else if (action instanceof IconActionBreak) {
                break;
            } else {
                String cmd = action.getCommand();
                if (replace != null && replace.size() > 0) {
                    replace.forEach((key, value) -> action.setCommand(action.getCommand().replace(key, value)));
                }
                action.onExecute(player, e, ArgsCache.getPlayerArgs(player));
                action.setCommand(cmd);
            }
        }

        actionListIterator.remove();
    }

    /**
     * 是否满足表达式
     *
     * @param requirement 表达式
     * @param player      玩家
     * @param event       容器事件
     * @return 是否满足
     */
    private static boolean isRequirementMatch(String requirement, Player player, InventoryClickEvent event) {
        if (requirement != null && !Strings.isEmpty(requirement)) {
            return (boolean) JavaScript.run(player, requirement, event, ArgsCache.getPlayerArgs(player));
        }
        return true;
    }

}
