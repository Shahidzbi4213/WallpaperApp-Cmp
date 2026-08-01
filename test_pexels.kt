import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader

fun main() {
    val url = URL("https://api.pexels.com/v1/curated?per_page=1")
    val connection = url.openConnection() as HttpURLConnection
    connection.setRequestProperty("Authorization", "WkLwI4236QJkE5w5x8hA7sBXXd0f4zE0Y8D8L5G7p9mZpDk2yTqZ0lK4") // Need a valid key, or I can just check the app's code for the key.
    connection.requestMethod = "GET"
    
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }
    reader.close()
}
