package com.google.wallpaperapp.ui.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.dynamicColorVisibility
import com.google.wallpaperapp.core.platform.getPlatformType
import com.google.wallpaperapp.ui.dialogs.AppModeDialog
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.utils.AppMode
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.app_lanuage
import wallpaperapp.composeapp.generated.resources.app_mode
import wallpaperapp.composeapp.generated.resources.dark
import wallpaperapp.composeapp.generated.resources.dynamic_color
import wallpaperapp.composeapp.generated.resources.feeback_icon
import wallpaperapp.composeapp.generated.resources.feedback
import wallpaperapp.composeapp.generated.resources.general
import wallpaperapp.composeapp.generated.resources.language_icon
import wallpaperapp.composeapp.generated.resources.light
import wallpaperapp.composeapp.generated.resources.off
import wallpaperapp.composeapp.generated.resources.on
import wallpaperapp.composeapp.generated.resources.others
import wallpaperapp.composeapp.generated.resources.privacy_policy
import wallpaperapp.composeapp.generated.resources.privacy_policy_icon
import wallpaperapp.composeapp.generated.resources.rate_us
import wallpaperapp.composeapp.generated.resources.rate_us_icon
import wallpaperapp.composeapp.generated.resources.share_app
import wallpaperapp.composeapp.generated.resources.share_app_icon
import wallpaperapp.composeapp.generated.resources.system_default

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navigateToLanguage: () -> Unit,
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel = koinViewModel()
) {

    val userPreference by settingViewModel.userPreference.collectAsStateWithLifecycle()
    val state by settingViewModel.state.collectAsStateWithLifecycle()

    val currentLanguage = remember { Locale.current.language }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(Res.string.general),
            maxLines = 1,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            textAlign = TextAlign.Start
        )

        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(
                    color = MaterialTheme.colorScheme
                        .surfaceContainer, shape = RoundedCornerShape(16.dp)
                )
                .border(
                    color = MaterialTheme.colorScheme
                        .outline,
                    width = 1.dp, shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {

            SettingsItem(
                title = Res.string.app_lanuage,
                description = if (currentLanguage.contains(userPreference.languageCode)) stringResource(
                    Res.string.system_default
                ) else "en",
                icon = Res.drawable.language_icon,
                onClick = navigateToLanguage
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))


            if (dynamicColorVisibility()) {
                SettingsItem(
                    title = Res.string.dynamic_color,
                    description =
                        if (userPreference.shouldShowDynamicColor) stringResource(Res.string.on).uppercase()
                        else stringResource(Res.string.off).uppercase(),
                    icon = Res.drawable.dynamic_color,
                    onClick = {
                        settingViewModel.onEvent(SettingEvent.ToggleDynamicDialog)
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
            }


            SettingsItem(
                title = Res.string.app_mode,
                description = when (AppMode.getModeById(userPreference.appMode)) {
                    AppMode.LIGHT -> stringResource(Res.string.light)
                    AppMode.DARK -> stringResource(Res.string.dark)
                    AppMode.DEFAULT -> stringResource(Res.string.system_default)
                },
                icon = Res.drawable.app_mode,
                onClick = {
                    settingViewModel.onEvent(SettingEvent.ToggleAppModeDialog)
                }
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.others),
            maxLines = 1,
            fontSize = 16.sp,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            textAlign = TextAlign.Start
        )

        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(
                    color = MaterialTheme.colorScheme
                        .surfaceContainer, shape = RoundedCornerShape(16.dp)
                )
                .border(
                    color = MaterialTheme.colorScheme
                        .outline,
                    width = 1.dp, shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            SettingsItem(
                title = Res.string.share_app,
                icon = Res.drawable.share_app_icon,
                onClick = {})

            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))

            SettingsItem(
                title = Res.string.rate_us,
                icon = Res.drawable.rate_us_icon,
                onClick = {
                    settingViewModel.onEvent(SettingEvent.ToggleRateUsDialog)
                })
            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
            SettingsItem(
                title = Res.string.feedback,
                icon = Res.drawable.feeback_icon,
                onClick = {})
            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
            SettingsItem(
                title = Res.string.privacy_policy,
                icon = Res.drawable.privacy_policy_icon,
                onClick = {})
        }
    }


    if (state.showAppModeDialog) {
        AppModeDialog(appMode = AppMode.getModeById(userPreference.appMode), onDismissRequest = {
            settingViewModel.onEvent(SettingEvent.ToggleAppModeDialog)
        }, onSelect = { updatedAppMode ->
            settingViewModel.onEvent(SettingEvent.UpdateAppMode(updatedAppMode))
        })
    }


}
