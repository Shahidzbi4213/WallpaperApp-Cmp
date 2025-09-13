package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.ActionIconBgColor
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.apply
import wallpaperapp.composeapp.generated.resources.download
import wallpaperapp.composeapp.generated.resources.favourite


@Composable
fun ActionButtons(
    isFavourite: Boolean = false,
    onDownload: () -> Unit,
    onApply: () -> Unit,
    onFavourite: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .safeDrawingPadding()
            .padding(bottom = 35.dp, start = 50.dp, end = 50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {

        Icon(
            imageVector = Icons.Outlined.Download,
            contentDescription =  stringResource(Res.string.download),
            modifier =  Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onDownload() }
                .background(color = ActionIconBgColor)
                .padding(8.dp),
            tint = Color.White
        )


        Button(
            onClick = onApply,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .padding(horizontal = 20.dp),

            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
        ) {

            Text(
                text = stringResource(Res.string.apply),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically)
            )
        }



        Image(
            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = stringResource(Res.string.favourite),
            colorFilter = ColorFilter.tint(color = Color.White),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onFavourite() }
                .background(color = ActionIconBgColor)
                .padding(8.dp)
        )


    }
}