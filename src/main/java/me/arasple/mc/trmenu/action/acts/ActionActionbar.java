package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 18:49
 */
public class ActionActionbar extends AbstractAction {

    @Override
    public String getName() {
        return "actionbar";
    }

    @Override
    public void onExecute(Player player) {
        TLocale.Display.sendActionBar(player, Vars.replace(player, getContent()));
    }

}
