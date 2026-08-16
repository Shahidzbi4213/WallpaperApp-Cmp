package com.google.wallpaperapp.ui.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.LocaleManager
import com.google.wallpaperapp.core.platform.WallpaperApplyResult
import com.google.wallpaperapp.core.platform.applyWallpaperFile
import com.google.wallpaperapp.core.platform.downloadsDir
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.desktopLocale
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.desktop.screens.DesktopCategoriesScreen
import com.google.wallpaperapp.ui.desktop.screens.DesktopDetailPane
import com.google.wallpaperapp.ui.desktop.screens.DesktopFavouriteScreen
import com.google.wallpaperapp.ui.desktop.screens.DesktopLanguageDialog
import com.google.wallpaperapp.ui.desktop.screens.DesktopMeshGradientScreen
import com.google.wallpaperapp.ui.desktop.screens.DesktopSettingsScreen
import com.google.wallpaperapp.ui.desktop.components.WallpaperPageGrid
import com.google.wallpaperapp.ui.desktop.paging.WallpaperFeed
import com.google.wallpaperapp.ui.desktop.paging.WallpaperPageLoader
import com.google.wallpaperapp.ui.desktop.paging.rememberWallpaperPageLoader
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import com.google.wallpaperapp.ui.screens.detail.SimilarWallpapersViewModel
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import com.google.wallpaperapp.ui.screens.languages.Language
import com.google.wallpaperapp.ui.screens.languages.LanguageViewModel
import com.google.wallpaperapp.ui.screens.search.SearchEvent
import com.google.wallpaperapp.ui.screens.search.SearchViewModel
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_empty_category
import wallpaperapp.composeapp.generated.resources.desktop_empty_category_sub
import wallpaperapp.composeapp.generated.resources.desktop_gradient_export_failed
import wallpaperapp.composeapp.generated.resources.desktop_no_results
import wallpaperapp.composeapp.generated.resources.desktop_no_results_sub
import wallpaperapp.composeapp.generated.resources.desktop_no_wallpapers
import wallpaperapp.composeapp.generated.resources.desktop_no_wallpapers_sub
import wallpaperapp.composeapp.generated.resources.desktop_saved_to
import wallpaperapp.composeapp.generated.resources.desktop_setting_wallpaper
import wallpaperapp.composeapp.generated.resources.desktop_wallpaper_set

@Composable
fun DesktopApp(controller: DesktopAppController = remember { DesktopAppController() }) {
    val locale by desktopLocale.collectAsStateWithLifecycle()

    DesktopTheme {
        // Compose Resources resolves strings against Locale.getDefault() at composition time and
        // does not observe changes, so a language switch remounts the tree rather than recomposing.
        key(locale) {
            DesktopAppContent(controller = controller)
        }
    }
}

@Composable
private fun DesktopAppContent(
    controller: DesktopAppController,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel(),
    similarViewModel: SimilarWallpapersViewModel = koinViewModel(),
    settingViewModel: SettingViewModel = koinViewModel(),
    languageViewModel: LanguageViewModel = koinViewModel()
) {
    val navState = rememberDesktopNavState()
    val actions = rememberDesktopWallpaperActions()
    val scope = rememberCoroutineScope()

    val searchFocusRequester = remember { FocusRequester() }
    var searchQuery by remember { mutableStateOf("") }
    var showLanguageDialog by remember { mutableStateOf(false) }

    // One loader per browsing context, so paging Home does not disturb a category or a search
    // and each keeps its own page number while you switch between them.
    val homeLoader = rememberWallpaperPageLoader()
    val categoryLoader = rememberWallpaperPageLoader()
    val searchLoader = rememberWallpaperPageLoader()

    val similarWallpapers = similarViewModel.similarWallpapers.collectAsLazyPagingItems()

    val favourites by favouriteViewModel.getAllFavourites.collectAsStateWithLifecycle()
    val userPreference by settingViewModel.userPreference.collectAsStateWithLifecycle()

    // Favourites are matched by Pexels id, so the same photo is recognised regardless of which
    // orientation url a given platform happened to save.
    val favouriteIds = remember(favourites) { favourites.map { it.id }.toSet() }

    val destination = navState.destination
    val preview = navState.preview
    val selectedSection = (destination as? DesktopDestination.Section)?.key ?: TopLevelBackStack.Home

    val title = when (destination) {
        is DesktopDestination.Section ->
            stringResource(bottomNavigationItems.first { it.key == destination.key }.label)

        is DesktopDestination.Category -> destination.name
        is DesktopDestination.Search -> "\"${destination.query}\""
    }

    // Point the right loader at whatever the desktop navigation is showing.
    LaunchedEffect(destination) {
        when (destination) {
            is DesktopDestination.Category ->
                categoryLoader.setFeed(WallpaperFeed.Search(destination.query))

            is DesktopDestination.Search -> {
                searchViewModel.onEvent(SearchEvent.OnQueryChange(destination.query))
                searchLoader.setFeed(WallpaperFeed.Search(destination.query))
            }

            is DesktopDestination.Section ->
                if (destination.key == TopLevelBackStack.Home) {
                    homeLoader.setFeed(WallpaperFeed.Curated)
                }
        }
    }

    LaunchedEffect(preview?.wallpaper?.id) {
        preview?.wallpaper?.let { similarViewModel.fetchSimilar(it.alt) }
    }

    // Let the window's menu bar and key handler drive this composition, and unhook on exit so a
    // late menu click cannot call into a composition that is gone.
    DisposableEffect(controller, navState) {
        controller.onFocusSearch = { runCatching { searchFocusRequester.requestFocus() } }
        controller.onSelectSection = { section ->
            searchQuery = ""
            navState.selectSection(section)
        }
        controller.onBackRequest = { navState.back() }
        onDispose {
            controller.onFocusSearch = null
            controller.onSelectSection = null
            controller.onBackRequest = null
        }
    }

    DesktopShell(
        selectedSection = selectedSection,
        title = title,
        searchQuery = searchQuery,
        searchFocusRequester = searchFocusRequester,
        canGoBack = navState.canGoBack,
        onSelectSection = {
            searchQuery = ""
            navState.selectSection(it)
        },
        onSearchChange = { query ->
            searchQuery = query
            if (query.isNotBlank()) {
                navState.openSearch(query)
            } else if (destination is DesktopDestination.Search) {
                navState.back()
            }
        },
        onBack = {
            if (destination is DesktopDestination.Search && preview == null) searchQuery = ""
            navState.back()
        }
    ) { isWide ->
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                DesktopContent(
                    destination = destination,
                    homeLoader = homeLoader,
                    categoryLoader = categoryLoader,
                    searchLoader = searchLoader,
                    favourites = favourites,
                    favouriteIds = favouriteIds,
                    selectedId = preview?.wallpaper?.id,
                    currentLanguageName = Language.findLanguageByCode(userPreference.languageCode).languageName,
                    navState = navState,
                    actions = actions,
                    favouriteViewModel = favouriteViewModel,
                    onLanguageClick = { showLanguageDialog = true },
                    onSaveRecentSearch = {
                        searchViewModel.onEvent(SearchEvent.SaveRecentSearch(it))
                    }
                )
            }

            // Master-detail on a wide window: the grid stays usable while previewing. On a
            // narrower one the pane takes the whole content area instead.
            AnimatedVisibility(
                visible = preview != null,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                val paneModifier = if (isWide) {
                    Modifier.width(DesktopDimens.DetailPaneDefaultWidth)
                } else {
                    Modifier.fillMaxSize()
                }
                preview?.let { current ->
                    DesktopDetailPane(
                        wallpaper = current.wallpaper,
                        isFavourite = current.wallpaper.id in favouriteIds,
                        similar = similarWallpapers,
                        onClose = { navState.closePreview() },
                        onApply = actions::apply,
                        onDownload = actions::download,
                        onToggleFavourite = { favouriteViewModel.addOrRemoveFavourite(it) },
                        onCopyLink = actions::copyLink,
                        onOpenPhotographer = actions::openPhotographer,
                        onOpenSimilar = { navState.openPreview(it, PreviewSource.SEARCH) },
                        modifier = paneModifier
                    )
                }
            }
        }
    }

    if (showLanguageDialog) {
        DesktopLanguageDialog(
            currentLanguageCode = userPreference.languageCode,
            onSelect = { language ->
                // Persist it, then swap the JVM locale -- the shell remounts on the second step.
                languageViewModel.updateCurrentLanguage(language)
                LocaleManager().changeLocale(language.languageCode)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

@Composable
private fun DesktopContent(
    destination: DesktopDestination,
    homeLoader: WallpaperPageLoader,
    categoryLoader: WallpaperPageLoader,
    searchLoader: WallpaperPageLoader,
    favourites: List<FavouriteWallpaper>,
    favouriteIds: Set<Long>,
    selectedId: Long?,
    currentLanguageName: String,
    navState: DesktopNavState,
    actions: DesktopWallpaperActions,
    favouriteViewModel: FavouriteViewModel,
    onLanguageClick: () -> Unit,
    onSaveRecentSearch: (String) -> Unit
) {
    val meshToast = remember { ToastManager() }
    val scope = rememberCoroutineScope()
    val settingMsg = stringResource(Res.string.desktop_setting_wallpaper)
    val setMsg = stringResource(Res.string.desktop_wallpaper_set)
    val savedMsg = stringResource(Res.string.desktop_saved_to)
    val exportFailedMsg = stringResource(Res.string.desktop_gradient_export_failed)

    when (destination) {
        is DesktopDestination.Category -> WallpaperPageGrid(
            state = categoryLoader.state,
            favouriteIds = favouriteIds,
            selectedId = selectedId,
            onPageSelected = categoryLoader::goToPage,
            onRetry = categoryLoader::retry,
            onOpen = { navState.openPreview(it, PreviewSource.CATEGORY) },
            onToggleFavourite = { favouriteViewModel.addOrRemoveFavourite(it) },
            onApply = actions::apply,
            onDownload = actions::download,
            onCopyUrl = actions::copyLink,
            onOpenPhotographer = actions::openPhotographer,
            emptyTitle = stringResource(Res.string.desktop_empty_category, destination.name),
            emptySubtitle = stringResource(Res.string.desktop_empty_category_sub)
        )

        is DesktopDestination.Search -> {
            LaunchedEffect(destination.query) { onSaveRecentSearch(destination.query) }
            WallpaperPageGrid(
                state = searchLoader.state,
                favouriteIds = favouriteIds,
                selectedId = selectedId,
                onPageSelected = searchLoader::goToPage,
                onRetry = searchLoader::retry,
                onOpen = { navState.openPreview(it, PreviewSource.SEARCH) },
                onToggleFavourite = { favouriteViewModel.addOrRemoveFavourite(it) },
                onApply = actions::apply,
                onDownload = actions::download,
                onCopyUrl = actions::copyLink,
                onOpenPhotographer = actions::openPhotographer,
                emptyTitle = stringResource(Res.string.desktop_no_results, destination.query),
                emptySubtitle = stringResource(Res.string.desktop_no_results_sub)
            )
        }

        is DesktopDestination.Section -> when (destination.key) {
            TopLevelBackStack.Home -> WallpaperPageGrid(
                state = homeLoader.state,
                favouriteIds = favouriteIds,
                selectedId = selectedId,
                onPageSelected = homeLoader::goToPage,
                onRetry = homeLoader::retry,
                onOpen = { navState.openPreview(it, PreviewSource.PAGED) },
                onToggleFavourite = { favouriteViewModel.addOrRemoveFavourite(it) },
                onApply = actions::apply,
                onDownload = actions::download,
                onCopyUrl = actions::copyLink,
                onOpenPhotographer = actions::openPhotographer,
                emptyTitle = stringResource(Res.string.desktop_no_wallpapers),
                emptySubtitle = stringResource(Res.string.desktop_no_wallpapers_sub)
            )

            TopLevelBackStack.Categories -> DesktopCategoriesScreen(
                onCategoryClick = { navState.openCategory(it.name, it.query) }
            )

            TopLevelBackStack.MeshGradients -> DesktopMeshGradientScreen(
                onApply = { preset ->
                    scope.launch {
                        meshToast.showToast(settingMsg)
                        val file = withContext(Dispatchers.IO) {
                            runCatching { exportMeshPreset(preset, meshExportFile(preset)) }
                        }
                        file.fold(
                            onSuccess = {
                                val result = applyWallpaperFile(it)
                                meshToast.showToast(
                                    (result as? WallpaperApplyResult.Failure)?.message ?: setMsg
                                )
                            },
                            onFailure = { meshToast.showToast(it.message ?: exportFailedMsg) }
                        )
                    }
                },
                onDownload = { preset ->
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            runCatching {
                                exportMeshPreset(
                                    preset,
                                    File(downloadsDir(), "screeny-gradient-${preset.name}.png")
                                )
                            }
                        }
                        meshToast.showToast(
                            result.fold(
                                onSuccess = { savedMsg.format(it.absolutePath) },
                                onFailure = { it.message ?: exportFailedMsg }
                            )
                        )
                    }
                }
            )

            TopLevelBackStack.Favourite -> DesktopFavouriteScreen(
                favourites = favourites,
                onOpen = { navState.openPreview(it, PreviewSource.FAVOURITE) },
                onRemove = { favouriteViewModel.addOrRemoveFavourite(it) },
                onApply = actions::apply,
                onDownload = actions::download,
                onExplore = { navState.selectSection(TopLevelBackStack.Home) }
            )

            TopLevelBackStack.Settings -> DesktopSettingsScreen(
                currentLanguageName = currentLanguageName,
                onLanguageClick = onLanguageClick
            )
        }
    }
}
