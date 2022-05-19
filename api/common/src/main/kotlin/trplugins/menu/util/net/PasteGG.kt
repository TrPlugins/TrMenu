package trplugins.menu.util.net

import taboolib.common.Isolated
import taboolib.common.env.DependencyDownloader
import trplugins.menu.util.parseJson
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Arasple
 * @date 2021/4/3 9:36
 */
@Isolated
object PasteGG {

    private const val URL = "https://api.paste.gg/v1/pastes"

    @JvmStatic
    fun main(args: Array<String>) {
        paste("Test", "Description", "Test Content", false, 1000 * 60, {
            println("Success: $it")
        }, {
            println("Failed")
        })
    }

    private fun getExpiresDate(lastMills: Long): String {
        val tz = TimeZone.getTimeZone("UTC");
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.timeZone = tz;
        return df.format(Date(System.currentTimeMillis() + lastMills));
    }

    private fun paste(
        name: String,
        description: String,
        content: String,
        public: Boolean,
        expires: Long,
        success: (String) -> Unit,
        failed: () -> Unit
    ) {
        try {
            val con = URL(URL).openConnection() as HttpURLConnection
            val json =
                """
                    {
                      "name": "$name",
                      "description": "$description",
                      "visibility": "${if (public) "public" else "unlisted"}",
                      "expires": "${getExpiresDate(expires)}",
                      "files": [
                        {
                          "name": "$name",
                          "content": {
                            "format": "text",
                            "highlight_language": null,
                            "value": "$content"
                          }
                        }
                      ]
                    }
                """.trimIndent()

            con.requestMethod = "POST"
            con.setRequestProperty("Charset", "UTF-8")
            con.doInput = true
            con.doOutput = true
            con.outputStream.also { it.write(json.toByteArray(StandardCharsets.UTF_8)) }
            val source = DependencyDownloader.readFully(con.inputStream, StandardCharsets.UTF_8).parseJson().asJsonObject

            success.invoke(source.toString())
        } catch (e: Throwable) {
            e.printStackTrace()
            failed.invoke()
        }
    }

}