package com.google.wallpaperapp.ui.dialogs;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.components.DialogComponentItem
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.apply
import wallpaperapp.composeapp.generated.resources.cancel
import wallpaperapp.composeapp.generated.resources.off
import wallpaperapp.composeapp.generated.resources.on

/**
 * Created by Shahid Iqbal on 29/1/25.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicColorDialog(
    modifier: Modifier = Modifier,
    shouldShowDynamicColor: Boolean,
    onDismissRequest: () -> Unit,
    onEnabled: () -> Unit,
    onDisabled: () -> Unit
) {


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .safeDrawingPadding(),
        dragHandle = { BottomSheetDefaults.DragHandle() }

    ) {

        DialogContent(
            shouldShowDynamicColor = shouldShowDynamicColor,
            onEnabled = onEnabled,
            onDisabled = onDisabled,
            onCancel = onDismissRequest
        )

    }
}


@Composable
private fun DialogContent(
    shouldShowDynamicColor: Boolean,
    onEnabled: () -> Unit,
    onDisabled: () -> Unit,
    onCancel: () -> Unit
) {

    var shouldEnable by remember { mutableStateOf(shouldShowDynamicColor) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        DialogComponentItem(
            text = stringResource(Res.string.on),
            isSelected = shouldEnable,
            onClick = { shouldEnable = true }
        )
        Spacer(modifier = Modifier.height(10.dp))

        DialogComponentItem(
            text = stringResource(Res.string.off),
            isSelected = !shouldEnable,
            onClick = { shouldEnable = false }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.Absolute.Right,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onCancel,
                modifier = Modifier.wrapContentSize(),
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor =
                            MaterialTheme.colorScheme
                                .onSurface
                    )
            ) {
                Text(text = stringResource(Res.string.cancel))
            }

            Spacer(modifier = Modifier.width(10.dp))

            TextButton(
                onClick = {
                    if (shouldEnable) onEnabled() else onDisabled()
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(Res.string.apply))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

    }


}


