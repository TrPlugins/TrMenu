package me.arasple.mc.traction.base;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/12/21 20:39
 */
public abstract class AbstractAction {

    public abstract String getName();

    private String content;
    private HashMap<EnumOption, String> options;

    public void run(Player player) {
        Metrics.increase(1);
        if (options.containsKey(EnumOption.CHANCE) && new Random().nextDouble() >= NumberUtils.toDouble(options.get(EnumOption.CHANCE), 1)) {
            return;
        }
        if (options.containsKey(EnumOption.REQUIREMENT) && !(boolean) JavaScript.run(player, options.get(EnumOption.REQUIREMENT))) {
            return;
        }
        if (options.containsKey(EnumOption.PLAYERS)) {
            Bukkit.getOnlinePlayers().stream().filter(p -> (boolean) JavaScript.run(p, options.get(EnumOption.PLAYERS))).collect(Collectors.toList()).forEach(this::onExecute);
            return;
        }
        if (options.containsKey(EnumOption.DELAY)) {
            int delay = NumberUtils.toInt(options.get(EnumOption.DELAY), -1);
            if (delay > 0) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.getPlugin(), () -> {
                    if (options.containsKey(EnumOption.PLAYERS)) {
                        Bukkit.getOnlinePlayers().stream().filter(p -> (boolean) JavaScript.run(p, getContent())).collect(Collectors.toList()).forEach(this::onExecute);
                        return;
                    }
                    onExecute(player);
                }, delay);
            }
            return;
        }

        onExecute(player);
    }

    /**
     * 供重写的动作内容
     *
     * @param player 执行玩家
     */
    public abstract void onExecute(Player player);

    /*
    GETTERS && SETTERS
     */

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<EnumOption, String> getOptions() {
        return options;
    }

    public String getOptionsAsString() {
        List<String> s = new ArrayList<>();
        options.forEach((type, value) -> s.add(type.toString(value)));
        return s.toString();
    }

    public void setOptions(HashMap<EnumOption, String> options) {
        this.options = options;
    }

    public AbstractAction create() {
        try {
            return getClass().getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "AbstractAction{" +
                "name='" + this.getClass().getName() + '\'' +
                "content='" + content + '\'' +
                ", options=" + options +
                '}';
    }

}
