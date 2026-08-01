package com.google.wallpaperapp.data.utils

object PagingDefaults {

    /**
     * Roughly what one aggregated page yields. Providers use their own page
     * sizes (Wallhaven is fixed at 24, Bing returns 8 once), so this is a hint
     * to Paging rather than an exact count.
     */
    const val PAGE_SIZE = 48
}
