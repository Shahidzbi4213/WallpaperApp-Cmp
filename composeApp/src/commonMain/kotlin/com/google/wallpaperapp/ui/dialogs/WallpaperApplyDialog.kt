package com.google.wallpaperapp.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.mirror
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.cancel
import wallpaperapp.composeapp.generated.resources.set_as_both
import wallpaperapp.composeapp.generated.resources.set_as_home_screen
import wallpaperapp.composeapp.generated.resources.set_as_lock_screen
import wallpaperapp.composeapp.generated.resources.wallpaper_dialog_title

private const val LAST_INDEX = 2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperApplyDialog(wallpaper: ImageBitmap?, onDismissRequest: () -> Unit) {


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier

    ) {
        DialogContent(wallpaper, onDismissRequest)
    }
}


@Composable
private fun DialogContent(wallpaper: ImageBitmap?, onCancel: () -> Unit) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 30.dp)
    ) {
        Text(
            text = stringResource(Res.string.wallpaper_dialog_title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W500),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .border(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            WallpaperActionItem.WALLPAPER_ACTION_ITEMS.forEachIndexed { index, item ->
                SingleActionItem(wallpaperActionItem = item, index = index, onClick = {})
            }

        }

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 10.dp, bottom = 5.dp),
        ) {
            Text(
                text = stringResource(Res.string.cancel),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }


    }


}

@Composable
private fun SingleActionItem(
    modifier: Modifier = Modifier,
    wallpaperActionItem: WallpaperActionItem,
    index: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(wallpaperActionItem.icon),
            contentDescription = null,
            modifier = Modifier
                .mirror()
                .size(35.dp)
                .padding(end = if (index == LAST_INDEX) 6.dp else 0.dp),
            colorFilter = ColorFilter.tint(color = LocalContentColor.current)
        )


        Text(
            text = stringResource(wallpaperActionItem.title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W300),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        )
    }

    if (index != LAST_INDEX)
        HorizontalDivider()
}


@Immutable
data class WallpaperActionItem(
    val icon: DrawableResource,
     val title: StringResource
) {

    companion object {

        val WALLPAPER_ACTION_ITEMS =
            listOf(
                WallpaperActionItem(Res.drawable.set_as_home_screen, Res.string.set_as_home_screen),
                WallpaperActionItem(Res.drawable.set_as_lock_screen, Res.string.set_as_lock_screen),
                WallpaperActionItem(Res.drawable.set_as_both, Res.string.set_as_both)
            )
    }
}