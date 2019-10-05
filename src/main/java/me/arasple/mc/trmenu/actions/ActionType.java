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
     * 发送一条 Actionbar 消息
     */
    ACTIONBAR("action", "actionbar"),

    /**
     * 向全体在线玩家发送一条 Actionbar 消息
     */
    ACTIONBAR_BROADCAST("actionall", "actionbarall", "action-all", "actionbar-all"),

    /**
     * 中断执行动作组
     */
    BREAK("break", "cancel"),

    /**
     * 向全体在线玩家发送一条消息
     */
    BROADCAST("broadcast", "announce"),

    /**
     * 向玩家发送一个聊天捕获器
     */
    CHAT_CATCHER("catch", "catcher", "catchers"),

    /**
     * 关闭当前菜单
     */
    CLOSE("close", "shut"),

    /**
     * 连接到指定 Bungee 服务器
     */
    CONNECT("connect", "bungee", "server"),

    /**
     * 通过后台执行某命令
     */
    CONSOLE("console"),

    /**
     * 延时多久再继续执行命令组
     */
    DELAY("delay", "wait"),

    /**
     * 执行脚本
     */
    JS("js", "script(s)?", "javascript(s)?"),

    /**
     * 向某玩家发送一条消息
     */
    MESSAGE("talk", "message", "send", "tell"),

    /**
     * 打开另一个 TrMenu 的菜单
     */
    OPEN("gui", "trmenu", "open"),

    /**
     * 以玩家身份执行命令
     */
    PLAYER_COMMAND("player", "command", "execute"),

    /**
     * 让玩家以OP身份执行命令
     */
    PLAYER_OP_COMMAND("op"),

    /**
     * 给玩家发送一个音效
     */
    SOUND("sound"),

    /**
     * 给全体在线挖掘发送一个音效
     */
    SOUND_ALL("sound-all", "soundall"),

    /**
     * 给玩家发送一个 Title + Subtitle
     */
    TITLE("title"),

    /**
     * 给全体在线玩家发送一个 Title + SubTitle
     */
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

    /**
     * 读字符串为一个动作
     *
     * @param line 字符串
     * @return 动作
     */
    public static BaseAction readAction(String line) {
        ActionType type = Arrays.stream(values()).filter(t -> Arrays.stream(t.names).anyMatch(x -> line.toLowerCase().split(":")[0].startsWith(x))).findFirst().orElse(ActionType.MESSAGE);
        String command = ArrayUtil.arrayJoin(line.split(":", 2), 1);
        String requirement = null;
        double chance = 1;
        StringBuilder value = new StringBuilder();

        // 读取相关参数
        for (Variables.Variable variable : new Variables(command).find().getVariableList()) {
            if (variable.isVariable()) {
                String[] args = variable.getText().split(":");
                if (args.length >= 2) {
                    if ("REQUIREMENT".equalsIgnoreCase(args[0])) {
                        requirement = args[1];
                        continue;
                    } else if ("CHANCE".equalsIgnoreCase(args[0])) {
                        chance = NumberUtils.toDouble(args[1], 1);
                        continue;
                    }
                }
                value.append("<").append(variable.getText()).append(">");
            } else {
                value.append(variable.getText());
            }
        }

        // 返回
        switch (type) {
            case ACTIONBAR:
                return new IconActionActionbar(value.toString()).setRequirement(requirement).setChance(chance);
            case ACTIONBAR_BROADCAST:
                return new IconActionActionbarBroadcast(value.toString()).setRequirement(requirement).setChance(chance);
            case BREAK:
                return new IconActionBreak(value.toString()).setRequirement(requirement).setChance(chance);
            case BROADCAST:
                return new IconActionBroadcast(value.toString()).setRequirement(requirement).setChance(chance);
            case CHAT_CATCHER:
                return new IconActionChatCatcher(value.toString()).setRequirement(requirement).setChance(chance);
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
