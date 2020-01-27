package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author Arasple
 * @date 2020/1/27 12:28
 */
public class ActionSetShape extends AbstractAction {

    @Override
    public String getName() {
        return "((set|switch)(-)?shape)|shape";
    }

    @Override
    public void onExecute(Player player) {
        Menu menu = TrMenuAPI.getMenu(player);
        int shapeIndex = NumberUtils.toInt(Vars.replace(player, getContent()), -1);
        if (menu != null && menu.getRows().size() > 1 && shapeIndex != menu.getShape(player)) {
            player.setMetadata("TrMenu.Force-Close", new FixedMetadataValue(TrMenu.getPlugin(), "Close"));
            menu.open(player, shapeIndex, true, ArgsCache.getPlayerArgs(player));
            player.removeMetadata("TrMenu.Force-Close", TrMenu.getPlugin());
        }
    }

}
