package trplugins.menu.module.internal.database

/**
 * @Author Rubenicos
 * @Since 2021-11-08 17:27
 */
enum class PlayerIndex {

    UUID, USERNAME;

    companion object {
        fun of(type: String): PlayerIndex {
            return try {
                valueOf(type.uppercase())
            } catch (ignored: Throwable) {
                UUID
            }
        }
    }
}