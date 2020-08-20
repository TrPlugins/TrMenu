package me.arasple.mc.trmenu

import com.google.gson.JsonNull
import com.google.gson.JsonParser
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2020/8/20 11:57
 */
@ExperimentalStdlibApi
fun main() {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm")

    val json = JsonParser().parse(readFromURL("https://bstats.org/api/v1/plugins/5742/charts/action_run_counts/data")).asJsonArray
    val map = buildMap<Long, Int> {
        json.forEach {
            it.asJsonArray.let { set ->
                this[set[0].asLong] = set[1].let { it ->
                    if (it is JsonNull) 0
                    else it.asInt
                }
            }
        }
    }
//        .filterKeys { it in 1597766400000..1597852800000 }
        .toSortedMap()

    map.forEach { (date, counts) ->
        println("Date: ${format.format(date)} -|- Counts: $counts")
    }

    println("From [${format.format(map.firstKey())}] to [${format.format(map.lastKey())}], Menus has been opened for over ${map.values.sum()} times.")
}

fun readFromURL(`in`: String?): String? {
    val connection = URL(`in`).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; GTB7.5; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)")
    val inputStream = connection.inputStream
    val bufferedInputStream = BufferedInputStream(inputStream)
    val stream = ByteArrayOutputStream()
    val buf = ByteArray(1024)
    var len: Int
    while (bufferedInputStream.read(buf).also { len = it } > 0) stream.write(buf, 0, len)
    return String(stream.toByteArray(), StandardCharsets.UTF_8)
}