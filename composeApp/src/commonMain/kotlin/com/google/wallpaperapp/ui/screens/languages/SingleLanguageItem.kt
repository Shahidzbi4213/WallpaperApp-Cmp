package com.google.wallpaperapp.ui.screens.languages


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.animation.AnimatedVisibility

@Composable
fun SingleLanguageItem(
    modifier: Modifier = Modifier,
    language: Language,
    isSelected: Boolean,
    canApplyBg: Boolean = true,
    onClick: (Language) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected && canApplyBg)
            MaterialTheme.colorScheme.secondaryContainer
        else Color.Transparent,
        label = "Language Bg Color",
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(),
        label = "Language Scale"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick(language) }
            .scale(scale)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = language.flag,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = language.languageName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(initialScale = 0.7f) + fadeIn(),
            exit = scaleOut(targetScale = 0.7f) + fadeOut()
        ) {
            RadioButton(
                selected = true,
                onClick = null,
                enabled = false,
                colors = RadioButtonDefaults.colors(
                    disabledSelectedColor = MaterialTheme.colorScheme.primary,
                    disabledUnselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                ),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
