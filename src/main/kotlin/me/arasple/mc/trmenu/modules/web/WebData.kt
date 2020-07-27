package me.arasple.mc.trmenu.modules.web

import io.izzel.taboolib.internal.gson.JsonElement
import io.izzel.taboolib.internal.gson.JsonParser
import joptsimple.internal.Strings
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
class WebData(val url: String, var data: String, val period: Long, val lastUpdated: Long, var updating: Boolean) {

    companion object {

        val CACHED_WEB_DATA = mutableListOf<WebData>()

        fun query(url: String): WebData = CACHED_WEB_DATA.firstOrNull { it.url.equals(url, true) } ?: WebData(url)

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

    @Suppress("DEPRECATION")
    fun getJsonElement(): JsonElement? = JsonParser().parse(get())

    fun hasData() = !Strings.isNullOrEmpty(data)

    fun update() {
        Tasks.run(true) {
            updating = true
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val inputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                val stream = ByteArrayOutputStream()
                val buf = ByteArray(1024)
                var len: Int
                while (bufferedInputStream.read(buf).also { len = it } > 0) stream.write(buf, 0, len)
                data = String(stream.toByteArray(), StandardCharsets.UTF_8)
                updating = false
            } catch (e: Throwable) {
                Msger.printErrors("WEB-DATA", e)
                updating = false
            }
        }
    }

    fun isExpired() = (System.currentTimeMillis() - lastUpdated) > period

}