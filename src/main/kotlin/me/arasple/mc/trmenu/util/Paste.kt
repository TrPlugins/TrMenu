package me.arasple.mc.trmenu.util

import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

/**
 * @author Arasple
 * @date 2020/8/30 11:57
 */
object Paste {

    @JvmStatic
    fun main(args: Array<String>) {
        println(
                post("Test Contenthahhhhhhhhh")
        )
    }

    fun post(content: String): String {
        val urlAPI = "https://mcpaste.io/api/post/create"
        val body = JsonObject().also {
            it.addProperty("content", content)
            it.addProperty("hideIps", true)
        }.toString()

        val url = URL(urlAPI)
        val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")
        con.connectTimeout = 15 * 1000
        con.doOutput = true

        /*
         * Send message through the https connection
         */
        try {
            val os: OutputStream = con.outputStream
            val input: ByteArray = body.toByteArray(StandardCharsets.UTF_8)
            os.write(input, 0, input.size)
            os.close()  // closing output stream
        } catch (e: IOException) {
            println("IO Exception: $e")
        }
        /*
         * Parse response
         */
        try {
            val br = BufferedReader(InputStreamReader(con.inputStream, StandardCharsets.UTF_8))
            val response = StringBuilder()
            br.use { reader ->
                reader.lineSequence().forEach {
                    response.append(it.trim())
                }
            }
            br.close() // closing input stream
            return response.toString()
        } catch (e: IOException) {
            println("Read response exeption: $e")
            return "Nothing"
        } finally {
            con.disconnect() // closing https connection
        }
    }

}