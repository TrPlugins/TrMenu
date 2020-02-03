package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.Variables;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.JsonItem;
import me.arasple.mc.trmenu.utils.Vars;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arasple
 * @date 2020/2/3 11:35
 * Better way to send JSON
 * Usage Example:
 * - 'json: &3Hello, &a%player_name%&3! Click <&a&nHERE?hover=&7Click to open website?url=https://trmenu.trixey.cn> &3to see our wiki.'
 */
public class ActionTellraw extends AbstractAction {

    private static Pattern pattern = Pattern.compile("<([^<>]+)>");
    private String rawJson;
    private TellrawJson tellraw;

    @Override
    public String getName() {
        return "json|tellraw";
    }

    @Override
    public void onExecute(Player player) {
        player.spigot().sendMessage(ComponentSerializer.parse(Vars.replace(player, Strings.nonEmpty(rawJson) ? rawJson : tellraw.toRawMessage())));
    }

    @Override
    public void setContent(String content) {
        content = TLocale.Translate.setColored(content);
        tellraw = TellrawJson.create();
        if (JsonItem.isJson(content)) {
            this.rawJson = content;
            return;
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            content = content.replace(matcher.group(), matcher.group().replace("?", "~"));
        }
        for (Variables.Variable v : new Variables(content).find().getVariableList()) {
            if (!v.getText().equals(content)) {
                if (!v.isVariable()) {
                    tellraw.append(v.getText());
                } else {
                    String[] args = v.getText().split("~");
                    tellraw.append(args[0]);
                    for (String s : ArrayUtils.remove(args, 0)) {
                        String[] event = s.split("=", 2);
                        if (event.length == 2) {
                            if ("hover".equalsIgnoreCase(event[0])) {
                                tellraw.hoverText(event[1]);
                            } else if ("suggest".equalsIgnoreCase(event[0])) {
                                tellraw.clickSuggest(event[1]);
                            } else if ("command".equalsIgnoreCase(event[0]) || "execute".equalsIgnoreCase(event[0])) {
                                tellraw.clickCommand(event[1]);
                            } else if ("url".equalsIgnoreCase(event[0]) || "open_url".equalsIgnoreCase(event[0])) {
                                tellraw.clickOpenURL(event[1]);
                            }
                        }
                    }
                }
            }
        }
    }

}
