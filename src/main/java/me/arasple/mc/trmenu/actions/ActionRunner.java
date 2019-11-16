package me.arasple.mc.trmenu.actions;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ext.IconActionBreak;
import me.arasple.mc.trmenu.actions.ext.IconActionDealy;
import me.arasple.mc.trmenu.actions.option.ActionOption;
import me.arasple.mc.trmenu.bstats.Metrics;
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
            actionGroups.putIfAbsent(baseAction.getOptions().getOrDefault(ActionOption.REQUIREMENT, null), new ArrayList<>());
            actionGroups.get(baseAction.getOptions().getOrDefault(ActionOption.REQUIREMENT, null)).add(baseAction);
        });
        List<BaseAction> actions = Lists.newArrayList();
        actionGroups.forEach((key, value) -> {
            if (isRequirementMatch(key, player, e)) {
                actions.addAll(value);
            }
        });
        executeActions(actions.listIterator(), player, e, replace);
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
            if (action.getOptions().containsKey(ActionOption.CHANCE) && new Random().nextDouble() >= NumberUtils.toDouble(action.getOptions().get(ActionOption.CHANCE), 1)) {
                continue;
            }
            // 延时参数执行
            if (action.getOptions().containsKey(ActionOption.DELAY)) {
                int delay = (int) NumberUtils.toDouble(action.getOptions().get(ActionOption.DELAY), -1);
                if (delay > 0) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.getPlugin(), () -> {
                        runAction(action, player, e, replace);
                    }, delay);
                }
                continue;
            }
            // 延时动作
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
                runAction(action, player, e, replace);
            }
        }
    }

    private static void runAction(BaseAction action, Player player, InventoryClickEvent e, Map<String, String> replace) {
        String cmd = action.getCommand();
        if (replace != null && replace.size() > 0) {
            replace.forEach((key, value) -> action.setCommand(action.getCommand().replace(key, value)));
        }
        action.onExecute(player, e);
        action.setCommand(cmd);
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
            return (boolean) JavaScript.run(player, requirement, event);
        }
        return true;
    }

}
