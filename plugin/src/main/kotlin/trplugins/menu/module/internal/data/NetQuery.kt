package trplugins.menu.module.internal.data

import com.google.common.collect.Maps
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import taboolib.common.platform.function.submit
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author Arasple
 * @date 2021/2/3 8:45
 */
class NetQuery(private val url: String, private val span: Int) {

    companion object {

        private val queries = Maps.newConcurrentMap<String, NetQuery>()

        fun query(url: String, span: Int): NetQuery {
            return queries.computeIfAbsent(url) { NetQuery(url, span) }
        }

    }

    private var lastUpdate = System.currentTimeMillis()
    private var updating = false
    private var data: String = ""

    private val shouldUpdate: Boolean
        get() {
            return System.currentTimeMillis() - lastUpdate > span * 1000
        }

    init {
        update()
    }

    fun get(): String {
        if (shouldUpdate) update()
        return data
    }

    fun has(): Boolean {
        return data.isNotBlank()
    }

    fun asJson(): JsonElement? {
        return JsonParser().parse(get())
    }

    fun update() {
        if (updating) return
        updating = true

        submit(async = true) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; GTB7.5; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)"
                )
                val inputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                val stream = ByteArrayOutputStream()
                val buf = ByteArray(1024)
                var len: Int
                while (bufferedInputStream.read(buf).also { len = it } > 0) stream.write(buf, 0, len)
                data = String(stream.toByteArray(), StandardCharsets.UTF_8)
                lastUpdate = System.currentTimeMillis()
                updating = false
            } catch (e: Throwable) {
                e.printStackTrace()
                updating = false
            }
        }
    }

}