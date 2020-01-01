package me.arasple.mc.traction.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arasple
 * @date 2019/12/21 20:40
 */
public enum EnumOption {

    /**
     * 执行动作的概率（小数）
     */
    CHANCE(0, "<(?i)(chance|rate):( )?([0-9]+[.]?[0-9]*>)"),

    /**
     * 延时执行该动作
     * 即先跳过该动作继续执行剩余的其它动作，延时后再执行
     */
    DELAY(1, "<(?i)(delay|wait):( )?([0-9]+[.]?[0-9]*>)"),

    /**
     * 对满足条件的所有在线玩家都执行该动作
     */
    PLAYERS(2, "<(?i)((for|all)?(-)?players)(:)?(.+)?>"),

    /**
     * 执行该动作需满足的表达式条件
     */
    REQUIREMENT(3, "<(?i)(require(ment)?|condition):( )?(.+>)");

    private int priority;
    private Pattern[] pattern;

    EnumOption(int priority, String pattern) {
        this.priority = priority;
        this.pattern = new Pattern[]{Pattern.compile(pattern + ">"), Pattern.compile(pattern)};
    }

        /*
        GETTERS & SETTERS
         */

    public int getPriority() {
        return priority;
    }

    public Matcher matcher(String content) {
        Matcher matcher = pattern[0].matcher(content);
        if (matcher.find()) {
            return pattern[1].matcher(matcher.group());
        } else {
            return pattern[1].matcher(content);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString(String value) {
        return "EnumOption{" +
                "name=" + name() +
                ", value=" + value +
                '}';
    }

}
