package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import com.google.wallpaperapp.ui.theme.Ember
import com.google.wallpaperapp.ui.theme.EmberGradient
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.Ink900
import com.google.wallpaperapp.ui.theme.TextLow
import org.jetbrains.compose.resources.stringResource

/**
 * Floating glass bottom navigation. The active tab lifts onto an ember
 * gradient pill with a neon glow — Screeny's futuristic take on the app's
 * Material NavigationBar (Home / Categories / Favourite / Settings).
 */
@Composable
fun BottomNavigationBar(
    selected: NavKey,
    onTabClick: (TopLevelBackStack) -> Unit
) {
    val pill = RoundedCornerShape(percent = 50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(18.dp, pill)
                .clip(pill)
                .background(Ink900.copy(alpha = 0.72f))
                .border(1.dp, GlassBorder, pill)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavigationItems.forEach { item ->
                val active = item.key == selected
                val label = stringResource(item.label)

                Row(
                    modifier = Modifier
                        .then(if (active) Modifier.weight(1f) else Modifier)
                        .height(46.dp)
                        .then(
                            if (active) Modifier.shadow(14.dp, pill, spotColor = Ember, ambientColor = Ember)
                            else Modifier
                        )
                        .clip(pill)
                        .then(if (active) Modifier.background(EmberGradient) else Modifier)
                        .clickable { onTabClick(item.key) }
                        .padding(horizontal = if (active) 20.dp else 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (active) item.selectedIcon else item.icon,
                        contentDescription = label,
                        tint = if (active) Color.White else TextLow,
                        modifier = Modifier.size(20.dp)
                    )
                    if (active) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
