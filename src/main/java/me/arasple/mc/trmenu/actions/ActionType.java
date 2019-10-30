package me.arasple.mc.trmenu.actions;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.Variables;
import me.arasple.mc.trmenu.actions.ext.*;
import me.arasple.mc.trmenu.actions.option.ActionOption;

import java.util.Arrays;
import java.util.HashMap;
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
    ACTIONBAR("action(bar)?"),

    /**
     * 向全体在线玩家发送一条 Actionbar 消息
     */
    ACTIONBAR_BROADCAST("actionall|actionbarall|action-all|actionbar-all"),

    /**
     * 中断执行动作组
     */
    BREAK("break|cancel"),

    /**
     * 向全体在线玩家发送一条消息
     */
    BROADCAST("broadcast|announce"),

    /**
     * 向玩家发送一个聊天捕获器
     */
    CHAT_CATCHER("catch(er)?|catcher(s)?"),

    /**
     * 关闭当前菜单
     */
    CLOSE("close|shut"),

    /**
     * 连接到指定 Bungee 服务器
     */
    CONNECT("connect|bungee|server"),

    /**
     * 通过后台执行某命令
     */
    CONSOLE("console"),

    /**
     * 延时多久再继续执行命令组
     */
    DELAY("delay|wait"),

    /**
     * 执行脚本
     */
    JS("js|(java)?script(s)?"),

    /**
     * 向某玩家发送一条消息
     */
    MESSAGE("talk|message|send|tell"),

    /**
     * 打开另一个 TrMenu 的菜单
     */
    OPEN("gui|trmenu|open"),

    /**
     * 以玩家身份执行命令
     */
    PLAYER_COMMAND("player|command|execute"),

    /**
     * 让玩家以OP身份执行命令
     */
    PLAYER_OP_COMMAND("op"),

    /**
     * 给玩家发送一个音效
     */
    SOUND("sound(s)?"),

    /**
     * 给全体在线挖掘发送一个音效
     */
    SOUND_ALL("sound(-)?all"),

    /**
     * 给玩家发送一个 Title + Subtitle
     */
    TITLE("title"),

    /**
     * 给全体在线玩家发送一个 Title + SubTitle
     */
    TITLE_BROADCAST("title(-)?all");

    private String name;

    ActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ActionType matchByName(String name) {
        return Arrays.stream(values()).filter(v -> name.matches("^(?i)" + v.getName())).findFirst().orElse(MESSAGE);
    }

    public static List<BaseAction> readAction(Object object) {
        if (object instanceof List) {
            return readAction((List<String>) object);
        } else {
            return Arrays.asList(readAction(String.valueOf(object)));
        }
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
        ActionType type = matchByName(line.split(":", 2)[0]);
        String command = ArrayUtil.arrayJoin(line.split(":", 2), 1);
        HashMap<ActionOption, String> options = new HashMap<>();
        StringBuilder value = new StringBuilder();
        List<Variables.Variable> vars = new Variables(command).find().getVariableList();

        // 读取相关参数
        for (Variables.Variable variable : vars) {
            if (variable.isVariable()) {
                String[] args = variable.getText().split(":");
                if (args.length >= 2) {
                    ActionOption option = ActionOption.matchType(args[0]);
                    if (option != null && !Strings.isEmpty(args[1])) {
                        options.put(option, args[1]);
                    }
                } else {
                    value.append("<").append(variable.getText()).append(">");
                }
            } else {
                value.append(variable.getText().replaceAll("<([^>]+?)>", ""));
            }
        }

        // 返回
        switch (type) {
            case ACTIONBAR:
                return new IconActionActionbar(value.toString(), options);
            case ACTIONBAR_BROADCAST:
                return new IconActionActionbarBroadcast(value.toString(), options);
            case BREAK:
                return new IconActionBreak(value.toString(), options);
            case BROADCAST:
                return new IconActionBroadcast(value.toString(), options);
            case CHAT_CATCHER:
                return new IconActionChatCatcher(value.toString(), options);
            case CLOSE:
                return new IconActionClose(value.toString(), options);
            case CONNECT:
                return new IconActionConnect(value.toString(), options);
            case CONSOLE:
                return new IconActionConsole(value.toString(), options);
            case DELAY:
                return new IconActionDealy(value.toString(), options);
            case JS:
                return new IconActionJs(value.toString(), options);
            case MESSAGE:
                return new IconActionMessage(value.toString(), options);
            case OPEN:
                return new IconActionOpen(value.toString(), options);
            case PLAYER_COMMAND:
                return new IconActionPlayerCommand(value.toString(), options);
            case PLAYER_OP_COMMAND:
                return new IconActionPlayerOpCommand(value.toString(), options);
            case SOUND:
                return new IconActionSound(value.toString(), options);
            case SOUND_ALL:
                return new IconActionSoundAll(value.toString(), options);
            case TITLE:
                return new IconActionTitle(value.toString(), options);
            case TITLE_BROADCAST:
                return new IconActionTitleBroadcast(value.toString(), options);
            default:
                return null;
        }
    }

}
