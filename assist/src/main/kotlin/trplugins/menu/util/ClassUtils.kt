package trplugins.menu.util

object ClassUtils {
    @JvmStatic
    val staticClass: Class<*> by lazy {
        if (System.getProperty("java.version").contains("1.8."))
            Class.forName("jdk.internal.dynalink.beans.StaticClass")
        else
            Class.forName("jdk.dynalink.beans.StaticClass")
    }

    @JvmStatic
    fun staticClass(className: String): Any? {
        return try {
            staticClass.getMethod("forClass", Class::class.java).invoke(null, Class.forName(className))
        } catch (e: Exception) {
            null
        }
    }
}