package com.google.wallpaperapp.ui.screens.languages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.LocaleManager
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.all_languages
import wallpaperapp.composeapp.generated.resources.selected_language


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    languageViewModel: LanguageViewModel = koinViewModel(),
    goBack: () -> Unit
) {

    val currentLanguage by languageViewModel.currentLanguage.collectAsStateWithLifecycle()
    val localSelected by languageViewModel.localSelected.collectAsStateWithLifecycle()
    val languagesList by languageViewModel.languagesList.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Toolbar(
            onBackPress = goBack,
            isApplyEnable = localSelected != null && localSelected != currentLanguage,
            onApply = {
                LocaleManager().changeLocale(localSelected!!.languageCode)
                languageViewModel.updateCurrentLanguage(localSelected!!)
                goBack()
            }
        )


        Spacer(modifier = Modifier.height(10.dp))

        Text(
            stringResource(Res.string.selected_language),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start,
        )


        SingleLanguageItem(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.95f),
            language = currentLanguage,
            isSelected = localSelected == null,
            canApplyBg = true,
            onClick = { languageViewModel.updateLocalLanguageSelection(null) })


        Spacer(modifier = Modifier.height(20.dp))
        Text(
            stringResource(Res.string.all_languages),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            fontFamily = getScreenyFontFamily(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Start,
        )


        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            overscrollEffect = null
        ) {
            items(items = languagesList, key = { language -> language.languageName })
            { language ->

                SingleLanguageItem(
                    language = language,
                    isSelected = localSelected == language,
                    onClick = languageViewModel::updateLocalLanguageSelection,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(0.95f)
                )
            }
        }
    }
}






