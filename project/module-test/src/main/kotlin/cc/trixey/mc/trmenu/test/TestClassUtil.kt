package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.test.util.println
import cc.trixey.mc.trmenu.util.fromClassesCollect

/**
 * TrMenu
 * cc.trixey.mc.trmenu.test.Test
 *
 * @author Score2
 * @since 2022/11/26 0:12
 */
object TestClassUtil {

    @JvmStatic
    fun main(args: Array<out String>) {
        val classes = mutableListOf<Class<*>>()

        classes.add(C1001::class.java)
        classes.add(C1002::class.java)
        classes.add(C1003::class.java)
        classes.add(C2001::class.java)
        classes.add(C2002::class.java)

        classes.fromClassesCollect(S1::class.java).forEach {
            it.name.println()
        }
    }

    abstract class S1
    abstract class S2

    class C1001 : S1()
    class C1002 : S1()
    class C1003 : S1()

    class C2001 : S2()
    class C2002 : S2()
}