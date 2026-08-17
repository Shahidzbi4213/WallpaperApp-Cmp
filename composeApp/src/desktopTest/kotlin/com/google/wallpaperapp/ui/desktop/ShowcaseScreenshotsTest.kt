package com.google.wallpaperapp.ui.desktop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.wallpaperapp.ui.desktop.components.CardActionButton
import com.google.wallpaperapp.ui.desktop.components.DesktopIconButton
import com.google.wallpaperapp.ui.desktop.DesktopShell
import com.google.wallpaperapp.ui.desktop.components.DesktopTooltip
import com.google.wallpaperapp.ui.desktop.components.PaginationBar
import com.google.wallpaperapp.ui.desktop.components.ScrollableGrid
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.theme.Crimson
import com.google.wallpaperapp.ui.theme.Ember
import com.google.wallpaperapp.ui.theme.EmberGradient
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.GlassBorderHi
import com.google.wallpaperapp.ui.theme.Ink950
import com.google.wallpaperapp.ui.theme.ScrimGradient
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.eyebrowStyle
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.skia.Image as SkiaImage
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class ShowcaseScreenshotsTest {

    private fun loadSampleImage(name: String): ImageBitmap? {
        val file = listOf(
            File("../build/sample_wallpapers/$name"),
            File("build/sample_wallpapers/$name"),
            File("composeApp/build/sample_wallpapers/$name")
        ).firstOrNull { it.exists() } ?: return null
        return runCatching {
            SkiaImage.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
        }.getOrNull()
    }

    private fun outPath(name: String): String {
        return if (File("../screenshots").exists()) "../screenshots/$name" else "screenshots/$name"
    }

    @Test
    fun `generate desktop showcase screenshots with real wallpapers`() {
        val sampleImages = (0..11).mapNotNull { loadSampleImage("nature_$it.jpg") }
        assertTrue(sampleImages.isNotEmpty(), "Sample wallpapers must be loaded")

        // 1. Fullscreen Lightbox Preview Screenshot
        val previewFile = renderToPng(outPath("desktop-preview.png"), 1440, 900) {
            DesktopTheme {
                val bgBitmap = sampleImages.getOrNull(3) ?: sampleImages.first()
                Box(Modifier.fillMaxSize().background(Ink950.copy(alpha = 0.94f))) {
                    Image(
                        bitmap = bgBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DesktopIconButton(
                            icon = Icons.Outlined.Close,
                            contentDescription = "Close",
                            onClick = {}
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Explore stunning snow-capped mountains cloaked in mist",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextHi,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "4 / 40",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextLow
                            )
                        }
                        Text(
                            text = "Click anywhere to toggle controls",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextLow.copy(alpha = 0.7f),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }

                    // Chevrons
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp)
                            .size(48.dp)
                            .glass(CircleShape, strong = true),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                            .size(48.dp)
                            .glass(CircleShape, strong = true),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    // Floating Glass HUD Island
                    val pillShape = RoundedCornerShape(16.dp)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
                            .glass(pillShape, strong = true)
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Column {
                                Text("PHOTOGRAPHER", style = eyebrowStyle(), color = TextLow, fontSize = 10.sp)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("eberhard grossgasteiger", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextHi)
                                    Icon(Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null, tint = TextMid, modifier = Modifier.size(15.dp))
                                }
                            }

                            Box(Modifier.height(32.dp).width(1.dp).background(Color.White.copy(alpha = 0.15f)))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(EmberGradient)
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Outlined.Wallpaper, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Set as wallpaper", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                    }
                                }

                                ActionBox(Icons.Outlined.Download)
                                ActionBox(Icons.Filled.Favorite, tint = Crimson)
                                ActionBox(Icons.Outlined.Link)
                            }
                        }
                    }
                }
            }
        }
        assertTrue(previewFile.length() > 0)

        // 2. Desktop Home with Grid + Master-Detail Pane
        val homeFile = renderToPng(outPath("desktop-home.png"), 1440, 900) {
            DesktopTheme {
                DesktopShell(
                    selectedSection = TopLevelBackStack.Home,
                    title = "Discover",
                    searchQuery = "",
                    searchFocusRequester = FocusRequester(),
                    canGoBack = false,
                    onSelectSection = {},
                    onSearchChange = {},
                    onBack = {}
                ) { isWide ->
                    Row(Modifier.fillMaxSize()) {
                        // Left Grid
                        Column(Modifier.weight(1f).fillMaxHeight()) {
                            ScrollableGrid(modifier = Modifier.weight(1f)) {
                                items(sampleImages.size.coerceAtMost(6)) { idx ->
                                    val isSel = idx == 3
                                    val shape = RoundedCornerShape(DesktopDimens.CardCorner)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(DesktopDimens.CardAspectRatio)
                                            .clip(shape)
                                            .border(if (isSel) 2.dp else 1.dp, if (isSel) Color.White.copy(alpha = 0.85f) else GlassBorder, shape)
                                    ) {
                                        Image(
                                            bitmap = sampleImages[idx],
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (idx == 0) {
                                            Box(Modifier.fillMaxSize().background(ScrimGradient))
                                            Row(
                                                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                CardActionButton(Icons.Outlined.Fullscreen, "Preview", onClick = {})
                                                CardActionButton(Icons.Outlined.Wallpaper, "Set", onClick = {})
                                                CardActionButton(Icons.Outlined.Download, "Download", onClick = {})
                                                CardActionButton(Icons.Filled.Favorite, "Fav", tint = Crimson, onClick = {})
                                            }
                                        }
                                    }
                                }
                            }

                            PaginationBar(currentPage = 1, totalPages = 42, enabled = true, onPageSelected = {})
                        }

                        // Right Detail Pane
                        val paneModifier = Modifier.width(DesktopDimens.DetailPaneDefaultWidth)
                        Column(
                            modifier = paneModifier
                                .fillMaxHeight()
                                .background(Ink950.copy(alpha = 0.72f))
                                .padding(DesktopDimens.Gutter)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Explore stunning snow-capped mountains",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextHi,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                DesktopIconButton(icon = Icons.Outlined.Fullscreen, contentDescription = "Full", onClick = {})
                                DesktopIconButton(icon = Icons.Outlined.Close, contentDescription = "Close", onClick = {})
                            }

                            Spacer(Modifier.height(14.dp))

                            val pShape = RoundedCornerShape(10.dp)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(210.dp)
                                    .clip(pShape)
                            ) {
                                Image(
                                    bitmap = sampleImages[3],
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("PHOTOGRAPHER", style = eyebrowStyle(), color = TextLow)
                                    Text("eberhard grossgasteiger", style = MaterialTheme.typography.bodyMedium, color = TextMid)
                                }
                                DesktopIconButton(icon = Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = "Open", onClick = {})
                            }

                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(EmberGradient)
                                        .padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Set as wallpaper", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                }
                                DesktopIconButton(icon = Icons.Outlined.Download, contentDescription = "Download", onClick = {})
                                DesktopIconButton(icon = Icons.Filled.Favorite, contentDescription = "Fav", onClick = {})
                                DesktopIconButton(icon = Icons.Outlined.Link, contentDescription = "Link", onClick = {})
                            }

                            Spacer(Modifier.height(20.dp))

                            Text("SIMILAR WALLPAPERS", style = eyebrowStyle(), color = TextLow)
                            Spacer(Modifier.height(10.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(4) { sIdx ->
                                    val simImg = sampleImages.getOrNull((sIdx + 4) % sampleImages.size)
                                    if (simImg != null) {
                                        val sShape = RoundedCornerShape(8.dp)
                                        Box(
                                            modifier = Modifier
                                                .width(110.dp)
                                                .aspectRatio(16f / 9f)
                                                .clip(sShape)
                                                .border(1.dp, GlassBorder, sShape)
                                        ) {
                                            Image(bitmap = simImg, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        assertTrue(homeFile.length() > 0)

        // 3. Desktop Categories Screen
        val categories = listOf(
            "Nature & Landscapes" to (sampleImages.getOrNull(1) ?: sampleImages.first()),
            "Architecture & City" to (sampleImages.getOrNull(2) ?: sampleImages.first()),
            "Space & Cosmos" to (sampleImages.getOrNull(8) ?: sampleImages.first()),
            "Minimal & Abstract" to (sampleImages.getOrNull(0) ?: sampleImages.first()),
            "Mountains & Forest" to (sampleImages.getOrNull(3) ?: sampleImages.first()),
            "Ocean & Waves" to (sampleImages.getOrNull(11) ?: sampleImages.first())
        )

        val catFile = renderToPng(outPath("desktop-categories.png"), 1440, 900) {
            DesktopTheme {
                DesktopShell(
                    selectedSection = TopLevelBackStack.Categories,
                    title = "Categories",
                    searchQuery = "",
                    searchFocusRequester = FocusRequester(),
                    canGoBack = false,
                    onSelectSection = {},
                    onSearchChange = {},
                    onBack = {}
                ) {
                    ScrollableGrid(modifier = Modifier.fillMaxSize()) {
                        items(categories.size) { cIdx ->
                            val (catName, catBitmap) = categories[cIdx]
                            val shape = RoundedCornerShape(DesktopDimens.CardCorner)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(DesktopDimens.CardAspectRatio)
                                    .clip(shape)
                                    .border(1.dp, GlassBorder, shape)
                            ) {
                                Image(
                                    bitmap = catBitmap,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Box(Modifier.fillMaxSize().background(ScrimGradient))

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = catName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextHi
                                    )
                                    Text(
                                        text = "Explore collection",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMid
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        assertTrue(catFile.length() > 0)

        // 4. Desktop Favourites Screen
        val favFile = renderToPng(outPath("desktop-favourites.png"), 1440, 900) {
            DesktopTheme {
                DesktopShell(
                    selectedSection = TopLevelBackStack.Favourite,
                    title = "Favourites",
                    searchQuery = "",
                    searchFocusRequester = FocusRequester(),
                    canGoBack = false,
                    onSelectSection = {},
                    onSearchChange = {},
                    onBack = {}
                ) {
                    ScrollableGrid(modifier = Modifier.fillMaxSize()) {
                        items(sampleImages.size.coerceAtMost(6)) { idx ->
                            val shape = RoundedCornerShape(DesktopDimens.CardCorner)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(DesktopDimens.CardAspectRatio)
                                    .clip(shape)
                                    .border(1.dp, GlassBorder, shape)
                            ) {
                                Image(
                                    bitmap = sampleImages[idx],
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                        .size(32.dp)
                                        .glass(CircleShape, strong = true),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = Crimson, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        assertTrue(favFile.length() > 0)
    }

    @Composable
    private fun ActionBox(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color = TextHi) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .glass(RoundedCornerShape(10.dp), strong = true),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
    }
}
