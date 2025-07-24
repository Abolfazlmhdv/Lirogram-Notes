package com.example.lirogram

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageHelper {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANG = "lang"


    fun saveLanguage(context: Context,lang: String) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_LANG,lang).apply()
    }

    fun getSavedLanguage(context: Context): String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_LANG,"en") ?: "en"
    }

    fun setLocale(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)
         return context.createConfigurationContext(config)
    }

}