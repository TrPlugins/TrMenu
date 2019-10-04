package me.arasple.mc.trmenu.actions;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trmenu.actions.ext.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Arasple
 * @date 2019/10/4 18:45
 */
public enum ActionType {

    /**
     * 点击图标执行的操作
     */

    CLOSE("close", "cancel", "shut"),
    CONNECT("connect", "bungee", "server"),
    CONSOLE("console"),
    MESSAGE("talk", "message", "send"),
    PLAYER_COMMAND("player", "command", "execute"),
    PLAYER_OP_COMMAND("op"),
    SOUND("sound");

    private String[] names;

    ActionType(String... names) {
        this.names = names;
    }

    public static List<BaseAction> readAction(List<String> lines) {
        if (lines == null) {
            return null;
        }

        List<BaseAction> actions = Lists.newArrayList();
        lines.forEach(l -> actions.add(readAction(l)));
        actions.removeIf(Objects::isNull);
        return actions;
    }

    public static BaseAction readAction(String line) {
        ActionType type = Arrays.stream(values()).filter(t -> Arrays.stream(t.names).anyMatch(x -> line.toLowerCase().split(":")[0].startsWith(x))).findFirst().orElse(ActionType.MESSAGE);
        String command = ArrayUtil.arrayJoin(line.split(":"), 0);

        switch (type) {
            case CLOSE:
                return new IconActionClose(command);
            case CONNECT:
                return new IconActionConnect(command);
            case CONSOLE:
                return new IconActionConsole(command);
            case MESSAGE:
                return new IconActionMessage(command);
            case PLAYER_COMMAND:
                return new IconActionPlayerCommand(command);
            case PLAYER_OP_COMMAND:
                return new IconActionPlayerOpCommand(command);
            case SOUND:
                return new IconActionSound(command);
            default:
                return null;
        }
    }

}
