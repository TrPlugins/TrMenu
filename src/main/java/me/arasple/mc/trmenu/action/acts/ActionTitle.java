package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Variables;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:03
 */
public class ActionTitle extends AbstractAction {

    private String title;
    private String subtitle;
    private int fadein = -1;
    private int stay = -1;
    private int fadeout = -1;

    @Override
    public String getName() {
        return "title|subtitle";
    }

    @Override
    public void onExecute(Player player) {
        String title = Vars.replace(player, this.title);
        String subTitle = Vars.replace(player, this.subtitle);
        TLocale.Display.sendTitle(player,
                title != null ? title : "",
                subTitle != null ? subTitle : "",
                fadein, stay, fadeout
        );
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
        initTitle();
    }

    public void initTitle() {
        if (getContent() == null) {
            return;
        }
        for (Variables.Variable variable : new Variables(getContent()).find().getVariableList()) {
            if (variable.isVariable()) {
                String[] x = variable.getText().split("=");
                if (x.length >= 2) {
                    String type = x[0].toLowerCase(), value = ArrayUtil.arrayJoin(x, 1);
                    switch (type) {
                        case "title":
                            this.title = value;
                            continue;
                        case "subtitle":
                            this.subtitle = value;
                            continue;
                        case "fadein":
                            this.fadein = NumberUtils.toInt(value, 20);
                            continue;
                        case "stay":
                            this.stay = NumberUtils.toInt(value, 40);
                            continue;
                        case "fadeout":
                            this.fadeout = NumberUtils.toInt(value, 20);
                            continue;
                        default:
                    }
                }
            }
        }
        this.fadein = fadein <= 0 ? 20 : fadein;
        this.fadeout = fadeout <= 0 ? 20 : fadeout;
        this.stay = stay <= 0 ? 40 : stay;
    }

}
