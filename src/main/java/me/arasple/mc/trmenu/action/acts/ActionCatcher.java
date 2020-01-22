package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Catchers;
import io.izzel.taboolib.util.lite.Signs;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.TrUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author Arasple
 * @date 2020/1/15 15:37
 * variable - $input
 * catcher: <type=SIGN/CHAT><require=TrUtils.isNumber("$input")><before=><valid=console:eco give %player_name% $input><invalid=tell:&cType a valid number...><cancel=xxx>
 * <p>
 * - |-
 * Catcher:
 * <chance:0.8>
 * <Type=CHAt>
 * <Before=Tell: &3&lPlease type a value><Valid=TELL:&6You typed a number &a$input>
 * <Invalid=TELL:&cInvalid number input>
 * <Require=function require(){
 * var blackList = ["word1", "word2"];
 * if (input.length<3 || input.length>8 || !/^\w+$/.test(input)){
 * return false;
 * }
 * for (var i=0; i<blackList.length; i++){
 * if (blackList[i].search(/$input/i) >= 0){
 * return false;
 * }
 * }
 * return true;
 * }
 * }require()>
 * <Cancel=TELL:&7Canceld...>
 */
public class ActionCatcher extends AbstractAction {

    private static boolean isCancelWord(String word) {
        return TrMenu.getSettings().getList("OPTIONS.CATCHER-CANCEL-WORDS", Collections.singletonList("quit|exit|cancel|return|break")).stream().anyMatch(k -> word.split(" ")[0].matches("(?i)" + k));
    }

    private int type;
    private String require;
    private String beforeInputAction;
    private String inputValidAction;
    private String inputInvalidAction;
    private String cancelAction;

    @Override
    public String getName() {
        return "catch(er)?";
    }

    @Override
    public void onExecute(Player player) {
        if (type == 0) {
            TrUtils.getInst().runAction(player, "close");
            Catchers.call(player, new Catchers.Catcher() {
                @Override
                public Catchers.Catcher before() {
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> TrUtils.getInst().runAction(player, beforeInputAction.replace(";", "_||_")), 3);
                    return this;
                }

                @Override
                public boolean after(String input) {
                    if (isCancelWord(input)) {
                        cancel();
                        return false;
                    }
                    if (require != null && !(boolean) JavaScript.run(player, require.replace("$input", input))) {
                        TrUtils.getInst().runAction(player, inputInvalidAction.replace(";", "_||_").replace("$input", input));
                        return true;
                    }
                    TrUtils.getInst().runAction(player, inputValidAction.replace(";", "_||_").replace("$input", input));
                    return false;
                }

                @Override
                public void cancel() {
                    TrUtils.getInst().runAction(player, cancelAction);
                }
            });
        } else {
            sendFakeSign(player);
        }
    }

    private void sendFakeSign(Player player) {
        Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> TrUtils.getInst().runAction(player, beforeInputAction.replace(";", "_||_")), 3);
        Signs.fakeSign(player, lines -> {
            String input = ArrayUtil.arrayJoin(lines, 0);
            if (isCancelWord(input)) {
                TrUtils.getInst().runAction(player, cancelAction.replace(";", "_||_"));
                return;
            }
            if (require != null && !(boolean) JavaScript.run(player, require.replace("$input", input))) {
                TrUtils.getInst().runAction(player, inputInvalidAction.replace(";", "_||_").replace("$input", input));
                sendFakeSign(player);
            } else {
                TrUtils.getInst().runAction(player, inputValidAction.replace(";", "_||_").replace("$input", input));
            }
        });
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);

        if (getContent() == null) {
            return;
        }

        type = 0;
        require = "";
        beforeInputAction = "";
        inputValidAction = "";
        inputInvalidAction = "";
        cancelAction = "";

        for (Variables.Variable v : new Variables(getContent()).find().getVariableList()) {
            if (v.isVariable()) {
                String[] x = v.getText().split("=", 2);
                if (x.length >= 2) {
                    String type = x[0].toLowerCase();
                    String value = ArrayUtil.arrayJoin(x, 1);
                    switch (type) {
                        case "type":
                            this.type = "CHAT".equalsIgnoreCase(value) ? 0 : 1;
                            continue;
                        case "require":
                            this.require = value;
                            continue;
                        case "before":
                            this.beforeInputAction = value;
                            continue;
                        case "valid":
                            this.inputValidAction = value;
                            continue;
                        case "invalid":
                            this.inputInvalidAction = value;
                            continue;
                        case "cancel":
                            this.cancelAction = value;
                            continue;
                        default:
                    }
                }
            }
        }
    }


}
