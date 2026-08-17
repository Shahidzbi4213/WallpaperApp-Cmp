package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.google.wallpaperapp.ui.desktop.components.desktopClickable
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import com.google.wallpaperapp.ui.screens.languages.LANGUAGES_LIST
import com.google.wallpaperapp.ui.screens.languages.Language
import com.google.wallpaperapp.ui.theme.Ember
import com.google.wallpaperapp.ui.theme.Ink900
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextMid
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_selected
import wallpaperapp.composeapp.generated.resources.language

/**
 * A real dialog window, not a modal bottom sheet. Bottom sheets are a touch idiom -- on desktop
 * a language picker is a small centred window you dismiss with Escape.
 */
@Composable
fun DesktopLanguageDialog(
    currentLanguageCode: String,
    onSelect: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    DialogWindow(
        onCloseRequest = onDismiss,
        state = rememberDialogState(width = 420.dp, height = 560.dp),
        title = stringResource(Res.string.language)
    ) {
        DesktopTheme {
            Box(modifier = Modifier.fillMaxSize().background(Ink900)) {
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(12.dp)
                ) {
                    items(LANGUAGES_LIST.size) { index ->
                        val language = LANGUAGES_LIST[index]
                        LanguageRow(
                            language = language,
                            selected = language.languageCode == currentLanguageCode,
                            onClick = { onSelect(language) }
                        )
                    }
                }

                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    language: Language,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DesktopDimens.RowHeight)
            .clip(RoundedCornerShape(8.dp))
            .desktopClickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(language.flag, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.width(12.dp))
        Text(
            text = language.languageName,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) TextHi else TextMid,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Icon(
                Icons.Outlined.Check,
                contentDescription = stringResource(Res.string.desktop_selected),
                tint = Ember,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
