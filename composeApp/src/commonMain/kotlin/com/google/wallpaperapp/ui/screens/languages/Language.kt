package com.google.wallpaperapp.ui.screens.languages

import com.google.wallpaperapp.ui.screens.languages.CountryFlags.getCountryFlag
import kotlin.collections.get


data class Language(
    val countryCode: String,
    val languageName: String,
    val languageCode: String,
    val flag: String
){

    companion object{
        fun findLanguageByCode(code: String): Language {
            return LANGUAGES_LIST.find { it.languageCode == code } ?: LANGUAGES_LIST[0]
        }
    }
}

val LANGUAGES_LIST by lazy {
    listOf(
        Language("sa", "Arabic", "ar", getCountryFlag("sa")),
        Language("gb", "English", "en", getCountryFlag("gb")),
        Language("ru", "Russian", "ru", getCountryFlag("ru")),
        Language("id", "Indonesian", "in", getCountryFlag("id")),
        Language("bd", "Bengali", "bn", getCountryFlag("bd")),
        Language("in", "Hindi", "hi", getCountryFlag("in")),
        Language("kp", "Korean", "ko", getCountryFlag("kp")),
        Language("jp", "Japanese", "ja", getCountryFlag("jp")),
        Language("cn", "Chinese", "zh", getCountryFlag("cn")),
        Language("pl", "Polish", "pl", getCountryFlag("pl")),
        Language("fr", "French", "fr", getCountryFlag("fr")),
        Language("it", "Italian", "it", getCountryFlag("it")),
        Language("in", "Tamil", "ta", getCountryFlag("in")),
        Language("pk", "Urdu", "ur", getCountryFlag("pk")),
        Language("de", "German", "de", getCountryFlag("de")),
        Language("es", "Spanish", "es", getCountryFlag("es")),
        Language("nl", "Dutch", "nl", getCountryFlag("nl")),
        Language("pt", "Portuguese", "pt", getCountryFlag("pt")),
        Language("th", "Thai", "th", getCountryFlag("th")),
        Language("tr", "Turkish", "tr", getCountryFlag("tr")),
        Language("ro", "Romanian", "ro", getCountryFlag("ro")),
        Language("ms", "Malay", "ro", getCountryFlag("ms")),
        Language("ir", "Persian", "fa", getCountryFlag("ir"))
    ).sortedBy { it.languageName }
}
