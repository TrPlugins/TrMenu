package me.arasple.mc.trmenu.actions;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Variables;
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

    ACTIONBAR("action", "actionbar"),
    ACTIONBAR_BROADCAST("actionall", "actionbarall", "action-all", "actionbar-all"),
    BREAK("break", "cancel"),
    BROADCAST("broadcast", "announce"),
    CLOSE("close", "shut"),
    CONNECT("connect", "bungee", "server"),
    CONSOLE("console"),
    DELAY("delay", "wait"),
    JS("js", "script(s)?", "javascript(s)?"),
    MESSAGE("talk", "message", "send", "tell"),
    OPEN("gui", "trmenu", "open"),
    PLAYER_COMMAND("player", "command", "execute"),
    PLAYER_OP_COMMAND("op"),
    SOUND("sound"),
    SOUND_ALL("sound-all", "soundall"),
    TITLE("title"),
    TITLE_BROADCAST("title-all", "titleall");

    private String[] names;

    ActionType(String... names) {
        this.names = names;
    }

    public static List<BaseAction> readAction(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return null;
        }

        List<BaseAction> actions = Lists.newArrayList();
        lines.forEach(l -> actions.add(readAction(l)));
        actions.removeIf(Objects::isNull);
        return actions;
    }

    public static BaseAction readAction(String line) {
        ActionType type = Arrays.stream(values()).filter(t -> Arrays.stream(t.names).anyMatch(x -> line.toLowerCase().split(":")[0].startsWith(x))).findFirst().orElse(ActionType.MESSAGE);
        String command = ArrayUtil.arrayJoin(line.split(":"), 1);
        String requirement = null;
        double chance = 1;
        StringBuilder value = new StringBuilder();

        for (Variables.Variable variable : new Variables(command).find().getVariableList()) {
            if (variable.isVariable()) {
                String[] args = variable.getText().split(":");
                if (args.length >= 2) {
                    if ("REQUIREMENT".equalsIgnoreCase(args[0])) {
                        requirement = args[1];
                        break;
                    }
                    if ("CHANCE".equalsIgnoreCase(args[0])) {
                        chance = NumberUtils.toDouble(args[1], 1);
                    }
                }
            } else {
                value.append(variable.getText());
            }
        }

        switch (type) {
            case ACTIONBAR:
                return new IconActionActionbar(value.toString()).setRequirement(requirement).setChance(chance);
            case ACTIONBAR_BROADCAST:
                return new IconActionActionbarBroadcast(value.toString()).setRequirement(requirement).setChance(chance);
            case BREAK:
                return new IconActionBreak(value.toString()).setRequirement(requirement).setChance(chance);
            case BROADCAST:
                return new IconActionBroadcast(value.toString()).setRequirement(requirement).setChance(chance);
            case CLOSE:
                return new IconActionClose(value.toString()).setRequirement(requirement).setChance(chance);
            case CONNECT:
                return new IconActionConnect(value.toString()).setRequirement(requirement).setChance(chance);
            case CONSOLE:
                return new IconActionConsole(value.toString()).setRequirement(requirement).setChance(chance);
            case DELAY:
                return new IconActionDealy(value.toString()).setRequirement(requirement).setChance(chance);
            case JS:
                return new IconActionJs(value.toString()).setRequirement(requirement).setChance(chance);
            case MESSAGE:
                return new IconActionMessage(value.toString()).setRequirement(requirement).setChance(chance);
            case OPEN:
                return new IconActionOpen(value.toString()).setRequirement(requirement).setChance(chance);
            case PLAYER_COMMAND:
                return new IconActionPlayerCommand(value.toString()).setRequirement(requirement).setChance(chance);
            case PLAYER_OP_COMMAND:
                return new IconActionPlayerOpCommand(value.toString()).setRequirement(requirement).setChance(chance);
            case SOUND:
                return new IconActionSound(value.toString()).setRequirement(requirement).setChance(chance);
            case SOUND_ALL:
                return new IconActionSoundAll(value.toString()).setRequirement(requirement).setChance(chance);
            case TITLE:
                return new IconActionTitle(value.toString()).setRequirement(requirement).setChance(chance);
            case TITLE_BROADCAST:
                return new IconActionTitleBroadcast(value.toString()).setRequirement(requirement).setChance(chance);
            default:
                return null;
        }
    }

}
