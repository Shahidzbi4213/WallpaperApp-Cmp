package com.google.wallpaperapp.ui.screens.languages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.apply
import wallpaperapp.composeapp.generated.resources.language


@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    isApplyEnable: Boolean,
    onBackPress: () -> Unit,
    onApply: () -> Unit
) {

    // Animate Apply button scale when enabled/disabled
    val applyScale by animateFloatAsState(
        targetValue = if (isApplyEnable) 1f else 0.9f,
        animationSpec = spring(),
        label = "Apply Button Scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPress,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = stringResource(Res.string.language),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )

        // Animated Apply button
        AnimatedVisibility(
            visible = isApplyEnable,
            enter = scaleIn(initialScale = 0.8f) + fadeIn(),
            exit = scaleOut(targetScale = 0.8f) + fadeOut()
        ) {
            TextButton(
                onClick = onApply,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .scale(applyScale),
                enabled = isApplyEnable
            ) {
                Text(
                    text = stringResource(Res.string.apply),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
