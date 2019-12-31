package me.arasple.mc.traction.acts;

import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.traction.base.AbstractAction;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:05
 */
public class ActionSound extends AbstractAction {

    @Override
    public String getName() {
        return "sound|playsound";
    }

    @Override
    public void onExecute(Player player) {
        new SoundPack(getContent()).play(player);
    }

}
