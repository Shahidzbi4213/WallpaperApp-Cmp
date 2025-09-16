package com.google.wallpaperapp.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.google.wallpaperapp.utils.AppMode
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.apply
import wallpaperapp.composeapp.generated.resources.cancel
import wallpaperapp.composeapp.generated.resources.dark
import wallpaperapp.composeapp.generated.resources.light
import wallpaperapp.composeapp.generated.resources.system_default

/**
 * Created by shahid-iqbal on 2/8/25
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModeDialog(
    modifier: Modifier = Modifier, appMode: AppMode, onDismissRequest: () -> Unit, onSelect: (AppMode) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest, modifier = modifier.systemBarsPadding(),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        DialogContent(appMode, onSelect, onDismissRequest)
    }
}

@Composable
private fun DialogContent(
    appMode: AppMode,
    onSelect: (AppMode) -> Unit,
    onCancel: () -> Unit,
) {

    var currentMode by remember { mutableStateOf(appMode) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        DialogComponentItem(text = stringResource(Res.string.system_default), isSelected = currentMode == AppMode.DEFAULT, onClick = { currentMode = AppMode.DEFAULT })
        Spacer(modifier = Modifier.height(10.dp))

        DialogComponentItem(text = stringResource(Res.string.light), isSelected = currentMode == AppMode.LIGHT, onClick = { currentMode = AppMode.LIGHT })

        Spacer(modifier = Modifier.height(10.dp))

        DialogComponentItem(text = stringResource(Res.string.dark), isSelected = currentMode == AppMode.DARK, onClick = { currentMode = AppMode.DARK })


        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp), horizontalArrangement = Arrangement.Absolute.Right, verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onCancel, modifier = Modifier.wrapContentSize(), colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = stringResource(Res.string.cancel))
            }

            Spacer(modifier = Modifier.width(10.dp))

            TextButton(
                onClick = { onSelect(currentMode) }, modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(Res.string.apply))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

    }


}