package menu.util.function;

/**
 * TrMenu
 * trmenu.util.function.Parser
 *
 * @author Score2
 * @since 2022/01/09 18:19
 */
public interface Parser<A, B, R> {

    R parse(A a, B b);

}
