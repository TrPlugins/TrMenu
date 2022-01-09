package trplugins.menu.util.function;

import taboolib.common.platform.ProxyPlayer;

/**
 * TrMenu
 * trmenu.util.function.VariableParser
 *
 * @author Score2
 * @since 2022/01/09 15:56
 */
public interface ContentParser extends Parser<ProxyPlayer, String, String> {

    ContentParser empty = (player, content) -> content;

}
