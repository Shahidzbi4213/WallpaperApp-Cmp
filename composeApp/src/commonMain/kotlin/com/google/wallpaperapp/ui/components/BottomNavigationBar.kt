package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    selected: TopLevelBackStack,
    onTabClick: (TopLevelBackStack) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        bottomNavigationItems.forEach { bottomNavItem ->
            val isSelected = bottomNavItem.key == selected

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onTabClick(bottomNavItem.key)
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) bottomNavItem.selectedIcon else bottomNavItem.icon,
                        contentDescription = stringResource(bottomNavItem.label),
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}