package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.Ember
import com.google.wallpaperapp.ui.theme.EmberGradient
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_next_page
import wallpaperapp.composeapp.generated.resources.desktop_page_of
import wallpaperapp.composeapp.generated.resources.desktop_previous_page
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.Ink950

/**
 * Numbered pagination. Desktop users want to know where they are and be able to go back to
 * exactly where they were -- infinite scroll gives you neither.
 *
 * Feeds run to hundreds of pages, so the bar shows a window around the current page with the
 * first and last always reachable, plus a jump box (clicking through to page 900 is not a plan).
 */
@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    if (totalPages <= 1) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            // Opaque backdrop and a hairline: without them the grid scrolls up behind the bar
            // and the two read as one surface.
            .background(Ink950.copy(alpha = 0.92f))
            .drawBehind {
                drawLine(
                    color = GlassBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f
                )
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowButton(
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
            description = stringResource(Res.string.desktop_previous_page),
            enabled = enabled && currentPage > 1,
            onClick = { onPageSelected(currentPage - 1) }
        )

        Spacer(Modifier.width(8.dp))

        pageWindow(currentPage, totalPages).forEach { slot ->
            when (slot) {
                is PageSlot.Number -> PageChip(
                    label = slot.page.toString(),
                    selected = slot.page == currentPage,
                    enabled = enabled,
                    onClick = { onPageSelected(slot.page) }
                )

                PageSlot.Gap -> Text(
                    text = "…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLow,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
            Spacer(Modifier.width(4.dp))
        }

        Spacer(Modifier.width(4.dp))

        ArrowButton(
            icon = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            description = stringResource(Res.string.desktop_next_page),
            enabled = enabled && currentPage < totalPages,
            onClick = { onPageSelected(currentPage + 1) }
        )

        Spacer(Modifier.width(20.dp))

        JumpToPage(totalPages = totalPages, enabled = enabled, onJump = onPageSelected)

        Spacer(Modifier.width(12.dp))

        Text(
            text = stringResource(Res.string.desktop_page_of, currentPage, totalPages),
            style = MaterialTheme.typography.bodySmall,
            color = TextLow
        )
    }
}

private sealed interface PageSlot {
    data class Number(val page: Int) : PageSlot
    data object Gap : PageSlot
}

/**
 * First, last, and a window around the current page, with gaps marked.
 * e.g. current 42 of 1655 -> 1 … 40 41 [42] 43 44 … 1655
 */
private fun pageWindow(current: Int, total: Int, radius: Int = 2): List<PageSlot> {
    if (total <= 7) return (1..total).map { PageSlot.Number(it) }

    val pages = sortedSetOf(1, total)
    for (p in (current - radius)..(current + radius)) {
        if (p in 1..total) pages.add(p)
    }
    // Keep the bar a stable width near the ends, where the window would otherwise be clipped.
    if (current <= radius + 2) (1..(radius * 2 + 3)).forEach { if (it <= total) pages.add(it) }
    if (current >= total - radius - 1) ((total - radius * 2 - 2)..total).forEach { if (it >= 1) pages.add(it) }

    val slots = mutableListOf<PageSlot>()
    var previous = 0
    for (page in pages) {
        if (previous != 0 && page - previous > 1) slots.add(PageSlot.Gap)
        slots.add(PageSlot.Number(page))
        previous = page
    }
    return slots
}

@Composable
private fun PageChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = RoundedCornerShape(8.dp)

    val base = Modifier
        .defaultMinSize(minWidth = 34.dp)
        .height(32.dp)
        .clip(shape)

    Box(
        modifier = when {
            selected -> base.background(EmberGradient)
            hovered && enabled -> base.background(Color.White.copy(alpha = 0.08f))
            else -> base
        }.desktopClickable(enabled = enabled && !selected, interaction = interaction, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else if (hovered) TextHi else TextMid,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun ArrowButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = RoundedCornerShape(8.dp)

    DesktopTooltip(text = description) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(shape)
                .then(
                    if (hovered && enabled) Modifier.background(Color.White.copy(alpha = 0.08f))
                    else Modifier
                )
                .desktopClickable(enabled = enabled, interaction = interaction, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                // Disabled arrows stay visible but obviously inert, rather than vanishing and
                // shifting the whole bar sideways at the first and last page.
                tint = if (!enabled) TextLow.copy(alpha = 0.4f) else if (hovered) TextHi else TextMid,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun JumpToPage(
    totalPages: Int,
    enabled: Boolean,
    onJump: (Int) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val shape = RoundedCornerShape(8.dp)

    fun submit() {
        val page = text.toIntOrNull() ?: return
        text = ""
        onJump(page.coerceIn(1, totalPages))
    }

    Row(
        modifier = Modifier.height(32.dp).width(66.dp).glass(shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (text.isEmpty()) {
                Text("Go to", style = MaterialTheme.typography.bodySmall, color = TextLow)
            }
            BasicTextField(
                value = text,
                // Digits only, so a stray letter cannot silently become a no-op submit.
                onValueChange = { input -> text = input.filter { it.isDigit() }.take(5) },
                enabled = enabled,
                singleLine = true,
                cursorBrush = SolidColor(Ember),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextHi,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(onGo = { submit() }, onDone = { submit() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
