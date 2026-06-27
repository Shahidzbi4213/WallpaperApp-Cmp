package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    selectedRoute: Routs,
    onTabClick: (Routs) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        bottomNavigationItems.forEach { bottomNavItem ->
            val isSelected = bottomNavItem.route == selectedRoute

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onTabClick(bottomNavItem.route)
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) bottomNavItem.selectedIcon else bottomNavItem.icon,
                        contentDescription = stringResource(bottomNavItem.name),
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}