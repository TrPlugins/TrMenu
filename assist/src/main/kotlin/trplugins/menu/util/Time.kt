package trplugins.menu.util

import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

/**
 * @author Arasple
 * @date 2021/2/4 10:58
 */
object Time {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val formatDate: () -> String = { dateFormat.format(System.currentTimeMillis()) }

    /**
     * @author Bkm016
     */
    fun parseDuration(string: String): Duration {
        var dur = string.uppercase(Locale.ENGLISH)
        if (!dur.contains("T")) {
            if (dur.contains("D")) {
                if (dur.contains("H") || dur.contains("M") || dur.contains("S")) dur = dur.replace("D", "DT")
            } else if (dur.startsWith("P")) dur = "PT" + dur.substring(1)
            else dur = "T$dur"
        }
        if (!dur.startsWith("P")) dur = "P$dur"

        return Duration.parse(dur)
    }

}