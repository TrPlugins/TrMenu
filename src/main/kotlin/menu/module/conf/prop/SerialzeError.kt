package menu.module.conf.prop

/**
 * @author Arasple
 * @date 2021/1/25 10:17
 */
inline class SerialzeError(val id: Int) {

    companion object {

        val INVALID_FILE = SerialzeError(0)
        val INVALID_ICON_UNDEFINED_TEXTURE = SerialzeError(1)

        fun formatInfo(error: SerialzeError): String {
            return when (error) {
                INVALID_FILE -> "Invalid file {0}. Menu configuration must be a YAML file"
                INVALID_ICON_UNDEFINED_TEXTURE -> "Invalid icon {0}. Icon must have a material texture, even if it is to be set AIR"
                else -> "Unknown error."
            }
        }

    }

}