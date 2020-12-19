package me.arasple.mc.trmenu.util

import io.izzel.taboolib.internal.gson.JsonParser
import io.izzel.taboolib.util.IO
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author Arasple
 * @date 2020/12/19 23:11
 */
object Paster {

    private const val URL = "https://paste.helpch.at/"

    fun paste(content: String): String? {
        return try {
            val con = URL("${URL}documents").openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("Charset", "UTF-8")
            con.doInput = true
            con.doOutput = true
            val os = con.outputStream.also { it.write(content.toByteArray(StandardCharsets.UTF_8)) }
            val source = JsonParser().parse(IO.readFully(con.inputStream, StandardCharsets.UTF_8)).asJsonObject
            URL + source.get("key").asString
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

}