// utils/StorageUtils.kt
package com.example.snacksave.utils

import android.content.Context
import com.example.snacksave.data.SpendingEntry
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import androidx.core.content.edit

object StorageUtils {
    private const val PREFS_NAME = "snacksave_prefs"
    private const val LOG_KEY = "spending_log"

    internal fun saveSpendingLog(context: Context, log: List<SpendingEntry>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            val json = Gson().toJson(log)
            putString(LOG_KEY, json.toString())
        }
    }

    private fun Gson() {
        TODO("Not yet implemented")
    }

    fun loadSpendingLog(context: Context): List<SpendingEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(LOG_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<SpendingEntry>>() {}.type
            Gson().fromJson(json)
        } else {
            emptyList()
        }
    }

}

private fun Unit.fromJson(
    string: String
): kotlin.collections.List<com.example.snacksave.data.SpendingEntry> {
    return TODO("Provide the return value")
}

private fun Unit.toJson(entries: List<SpendingEntry>) {}
