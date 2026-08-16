package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.core.platform.appDataDir
import com.google.wallpaperapp.core.platform.currentOs
import com.google.wallpaperapp.core.platform.downloadsDir
import com.google.wallpaperapp.ui.desktop.components.desktopClickable
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.eyebrowStyle
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_appdata_folder
import wallpaperapp.composeapp.generated.resources.desktop_downloads_folder
import wallpaperapp.composeapp.generated.resources.desktop_group_about
import wallpaperapp.composeapp.generated.resources.desktop_group_general
import wallpaperapp.composeapp.generated.resources.desktop_group_locations
import wallpaperapp.composeapp.generated.resources.desktop_photos
import wallpaperapp.composeapp.generated.resources.desktop_photos_value
import wallpaperapp.composeapp.generated.resources.desktop_platform
import wallpaperapp.composeapp.generated.resources.language

/**
 * A desktop preferences page: content capped at a readable width and left-aligned, rather than
 * the phone's `fillMaxWidth(0.95f)` cards which would stretch across an ultrawide display.
 */
@Composable
fun DesktopSettingsScreen(
    currentLanguageName: String,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = DesktopDimens.Gutter, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.widthIn(max = DesktopDimens.FormMaxWidth)) {

                SettingsGroup(title = stringResource(Res.string.desktop_group_general)) {
                    SettingsRow(
                        label = stringResource(Res.string.language),
                        value = currentLanguageName,
                        onClick = onLanguageClick
                    )
                }

                Spacer(Modifier.height(24.dp))

                SettingsGroup(title = stringResource(Res.string.desktop_group_locations)) {
                    // Desktop users expect to know where their files actually landed.
                    SettingsRow(label = stringResource(Res.string.desktop_downloads_folder), value = downloadsDir().absolutePath)
                    SettingsRow(label = stringResource(Res.string.desktop_appdata_folder), value = appDataDir().absolutePath)
                }

                Spacer(Modifier.height(24.dp))

                SettingsGroup(title = stringResource(Res.string.desktop_group_about)) {
                    SettingsRow(label = stringResource(Res.string.desktop_platform), value = currentOs.name.lowercase()
                        .replaceFirstChar { it.uppercase() })
                    SettingsRow(label = stringResource(Res.string.desktop_photos), value = stringResource(Res.string.desktop_photos_value))
                }

                Spacer(Modifier.height(32.dp))
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(2.dp)
        )
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = eyebrowStyle(),
            color = TextLow,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glass(RoundedCornerShape(12.dp))
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            content = { content() }
        )
    }
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    val base = Modifier
        .fillMaxWidth()
        .height(DesktopDimens.RowHeight + 6.dp)

    Row(
        modifier = if (onClick != null) base.desktopClickable(onClick = onClick) else base,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextHi,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = TextMid,
            modifier = Modifier.padding(end = if (onClick != null) 4.dp else 16.dp)
        )
        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = TextLow,
                modifier = Modifier.padding(end = 12.dp).size(18.dp)
            )
        }
    }
}
