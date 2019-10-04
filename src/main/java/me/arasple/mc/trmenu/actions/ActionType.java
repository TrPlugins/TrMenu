package me.arasple.mc.trmenu.actions;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trmenu.actions.ext.IconActionConsole;
import me.arasple.mc.trmenu.actions.ext.IconActionMessage;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 18:45
 */
public enum ActionType {

    /**
     * 点击图标执行的操作
     */

    MESSAGE("talk", "message", "send"),
    CONSOLE("console");

    private String[] names;

    ActionType(String... names) {
        this.names = names;
    }

    public static List<BaseAction> readAction(List<String> lines) {
        List<BaseAction> actions = Lists.newArrayList();
        lines.forEach(l -> actions.add(readAction(l)));
        return actions;
    }

    public static BaseAction readAction(String line) {
        ActionType type = Arrays.stream(values()).filter(t -> Arrays.stream(t.names).anyMatch(x -> line.toLowerCase().split(":")[0].startsWith(x))).findFirst().orElse(ActionType.MESSAGE);
        String command = ArrayUtil.arrayJoin(line.split(":"), 0);

        switch (type) {
            case CONSOLE:
                return new IconActionConsole(command);
            case MESSAGE:
                return new IconActionMessage(command);
            default:
                return null;
        }
    }

}
