import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.google.wallpaperapp.di.initKoin
import com.google.wallpaperapp.ui.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    val loading = document.getElementById("loading")
    loading?.remove()
    ComposeViewport(document.body!!) {
        App()
    }
}
