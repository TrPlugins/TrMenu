package me.arasple.mc.traction;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import me.arasple.mc.traction.acts.*;
import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.traction.base.EnumOption;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;

/**
 * @author Arasple
 * @date 2019/12/21 20:33
 * TrMenu internal part of TrAction
 */
public class TrAction {

    private static List<AbstractAction> actions = Arrays.asList(
            new ActionActionbar(),
            new ActionBreak(),
            new ActionClose(),
            new ActionCommand(),
            new ActionCommandOp(),
            new ActionConnect(),
            new ActionConsole(),
            new ActionDelay(),
            new ActionJs(),
            new ActionOpen(),
            new ActionSound(),
            new ActionTell(),
            new ActionTitle()
    );

    public static void runActions(ListIterator<AbstractAction> actions, Player player) {
        while (actions.hasNext()) {
            AbstractAction action = actions.next();
//            player.sendMessage(ChatColor.GRAY + "--------------------");
//            player.sendMessage("Action " + ChatColor.AQUA + action.toString());
//            player.sendMessage(ChatColor.GRAY + "--------------------");
            if (action instanceof ActionBreak) {
                break;
            } else if (action instanceof ActionDelay) {
                double delay = NumberUtils.toDouble(action.getContent(), -1);
                if (delay > 0) {
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> runActions(actions, player), (long) delay);
                }
                break;
            }
            action.run(player);
        }
    }

    /**
     * 同行并列多个动作, 动作选项共享
     * 分割符号 “_||_”
     *
     * @param lines 行
     * @return 动作
     */
    public static List<AbstractAction> readActions(List<String> lines) {
        List<AbstractAction> actions = new ArrayList<>();
        lines.forEach(line -> actions.addAll(readActions(line)));
        return actions;
    }

    public static List<AbstractAction> readActions(String line) {
        List<AbstractAction> actions = new ArrayList<>();
        HashMap<EnumOption, String> options = new HashMap<>();
        for (String s : line.split("_\\|\\|_")) {
            AbstractAction read = readSingleAction(s);
            if (read != null) {
                actions.add(read);
                if (read.getOptions() != null) {
                    options.putAll(read.getOptions());
                }
            }
        }
        if (actions.size() > 1) {
            actions.forEach(act -> act.setOptions(options));
        }
        return actions;
    }

    /**
     * 读单个动作
     *
     * @param line 行
     * @return 动作
     */
    private static AbstractAction readSingleAction(String line) {
        String[] acts = line.replaceFirst("( )?:( )?", ":").split(":", 2);
        AbstractAction action = actions.stream().filter(act -> acts[0].matches(act.getName())).findFirst().orElse(null);
        HashMap<EnumOption, String> options = new HashMap<>();

        if (action == null) {
            return null;
        } else {
            action = action.create();
            line = acts.length == 2 ? acts[1] : acts[0];
        }

        for (EnumOption option : EnumOption.values()) {
            Matcher matcher = option.matcher(line);
            while (matcher.find()) {
                String find = matcher.group();
                String[] opts = find.split(":", 2);
                String value = opts.length >= 2 ? opts[1].substring(0, opts[1].length() - 1) : null;
                options.put(option, value);
                line = line.replace(find, "");
            }
        }

        action.setContent(line);
        action.setOptions(options);
        return action;
    }

}
