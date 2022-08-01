package trplugins.menu.module.conf.prop

/**
 * @author Arasple
 * @date 2021/1/25 10:17
 */
@JvmInline
value class SerializeError(val id: Int) {

    companion object {

        val INVALID_FILE = SerializeError(0)
        val INVALID_ICON_UNDEFINED_TEXTURE = SerializeError(1)

        fun formatInfo(error: SerializeError): String {
            return when (error) {
                INVALID_FILE -> "Invalid file {0}. The menu configuration format is not supported"
                INVALID_ICON_UNDEFINED_TEXTURE -> "Invalid icon {0}. Icon must have a material texture, even if it is to be set AIR"
                else -> "Unknown error."
            }
        }

    }

}