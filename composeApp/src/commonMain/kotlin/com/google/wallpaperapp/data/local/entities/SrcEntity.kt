package com.google.wallpaperapp.data.local.entities


data class SrcEntity(
    val medium: String,
    val portrait: String,
    val small: String,
    // Added in schema v3. Rows cached before the upgrade carry "" and fall back to portrait
    // until the next curated refresh backfills them.
    val landscape: String = "",
    val original: String = ""
)