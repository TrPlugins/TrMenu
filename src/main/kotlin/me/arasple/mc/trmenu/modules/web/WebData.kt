package me.arasple.mc.trmenu.modules.web

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Tasks
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


/**
 * @author Arasple
 * @date 2020/7/27 15:11
 */
data class WebData(val url: String, var data: String, val period: Long, var lastUpdated: Long, var updating: Boolean) {

    companion object {

        val CACHED_WEB_DATA = mutableListOf<WebData>()

        fun query(url: String) = CACHED_WEB_DATA.firstOrNull { it.url == url } ?: WebData(url).let {
            CACHED_WEB_DATA.add(it)
            it
        }

    }

    constructor(url: String) : this(url, 30 * 1000)

    constructor(url: String, period: Long) : this(url, "", period, -1, false)

    init {
        update()
    }

    fun get(): String {
        if (isExpired()) update()
        return data
    }

    fun getJsonElement(): JsonElement? = JsonParser().parse(get())

    fun hasData() = !Strings.isBlank(data)

    fun update() {
        if (updating) return
        Tasks.task(true) {
            updating = true
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; GTB7.5; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)");
                val inputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                val stream = ByteArrayOutputStream()
                val buf = ByteArray(1024)
                var len: Int
                while (bufferedInputStream.read(buf).also { len = it } > 0) stream.write(buf, 0, len)
                data = String(stream.toByteArray(), StandardCharsets.UTF_8)
                lastUpdated = System.currentTimeMillis()
                updating = false
            } catch (e: Throwable) {
                Msger.printErrors("WEB-DATA", e, url)
                updating = false
            }
        }
    }

    fun isExpired() = (System.currentTimeMillis() - lastUpdated) > period

}