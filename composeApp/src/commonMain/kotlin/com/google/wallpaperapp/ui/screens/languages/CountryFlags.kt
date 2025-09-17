package com.google.wallpaperapp.ui.screens.languages


object CountryFlags {



    fun getCountryFlag(countryCode: String): String {
        require(countryCode.length == 2) { "Country code must be 2 letters" }
        return buildString {
            countryCode.uppercase().forEach { char ->
                val base = 0x1F1E6
                val offset = char.code - 'A'.code
                append(getByUnicode(base + offset))
            }
        }
    }

    // Reuse the surrogate-safe function
    fun getByUnicode(unicode: Int): String {
        return if (unicode <= 0xFFFF) {
            unicode.toChar().toString()
        } else {
            val high = ((unicode - 0x10000) shr 10) + 0xD800
            val low = ((unicode - 0x10000) and 0x3FF) + 0xDC00
            buildString {
                append(high.toChar())
                append(low.toChar())
            }
        }
    }



}
