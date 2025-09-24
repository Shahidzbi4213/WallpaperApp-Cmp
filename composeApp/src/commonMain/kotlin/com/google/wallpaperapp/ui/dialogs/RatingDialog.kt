package com.google.wallpaperapp.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.one_star
import wallpaperapp.composeapp.generated.resources.rate_us
import wallpaperapp.composeapp.generated.resources.we_work
import wallpaperapp.composeapp.generated.resources.your_opinion_matters_to_us

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingDialog(onDismiss: () -> Unit) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        containerColor = Color.Transparent,
        modifier = Modifier.navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .shadow(1.dp)
        ) {

            Image(
                painter =
                    painterResource(Res.drawable.one_star),
                contentDescription = null,
                modifier = Modifier.offset(y = (40).dp)
                    .zIndex(1f)
                    .padding(horizontal = 15.dp)
                    .shadow(100.dp)
            )



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().heightIn(min = 350.dp)
                    .background(BottomSheetDefaults.ContainerColor, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {

                Icon(
                    imageVector = Icons.Default.Cancel, contentDescription = null,
                    modifier = Modifier.align(Alignment.End)
                        .clickable(){onDismiss()}
                        .padding(20.dp)

                )

                Text(
                    stringResource(Res.string.rate_us),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = getScreenyFontFamily(),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(5.dp))
                Text(stringResource(Res.string.your_opinion_matters_to_us),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ))

                Spacer(Modifier.height(20.dp))
                Text(stringResource(Res.string.we_work),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    lineHeight = 25.sp,
                    modifier = Modifier.fillMaxWidth(0.85f))


            }

        }

    }
}