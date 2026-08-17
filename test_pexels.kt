import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.util.Properties

fun main() {
    val localProps = File("local.properties")
    val props = Properties().apply {
        if (localProps.exists()) FileInputStream(localProps).use { load(it) }
    }
    val apiKey = props.getProperty("PEXELS_API_KEY") ?: System.getenv("PEXELS_API_KEY") ?: ""

    val url = URL("https://api.pexels.com/v1/curated?per_page=1")
    val connection = url.openConnection() as HttpURLConnection
    connection.setRequestProperty("Authorization", apiKey)
    connection.requestMethod = "GET"
    
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }
    reader.close()
}
