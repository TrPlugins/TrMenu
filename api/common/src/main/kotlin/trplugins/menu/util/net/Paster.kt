package trplugins.menu.util.net

import taboolib.common.env.DependencyDownloader
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import trplugins.menu.util.parseJson
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author Arasple
 * @date 2020/12/19 23:11
 */
object Paster {

    private const val URL = "https://paste.helpch.at/"

    fun paste(sender: ProxyCommandSender, content: String, extension: String = "") {
        sender.sendLang("Paster-Processing")
        paste(
            content,
            { sender.sendLang("Paster-Success", "$it.$extension") },
            { sender.sendLang("Paster-Failed") }
        )
    }

    private fun paste(content: String, url: (String) -> Unit, failed: () -> Unit) {
        submit(async = true) {
            try {
                val con = URL("${URL}documents").openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("Charset", "UTF-8")
                con.doInput = true
                con.doOutput = true
                con.outputStream.also { it.write(content.toByteArray(StandardCharsets.UTF_8)) }
                val source = DependencyDownloader.readFully(con.inputStream, StandardCharsets.UTF_8).parseJson().asJsonObject
                url(URL + source.get("key").asString)
            } catch (e: Throwable) {
                e.printStackTrace()
                failed.invoke()
            }
        }
    }

}