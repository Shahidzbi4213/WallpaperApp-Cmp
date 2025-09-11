package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),

        ) {
        bottomNavigationItems.forEach { bottomNavItem ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavItem.route::class.qualifiedName } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(bottomNavItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
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