package com.eipsaferoad.owl.utils

import android.content.Context

enum class KeysEnum(val value: String) {
    ALARM_VIBRATION("alarm_vibration"),
    ALARM_SOUND("alarm_sound"),
    ALARM_ACTIVATE("alarm_activate"),
    EMAIL("email"),
    PASSWORD("password")
}

class LocalStorage {

    companion object {

        fun setData(context: Context, key: String, value: String) {
            val sharedPref = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun getData(context: Context, key: String): String? {
            val sharedPref = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE) ?: return null
            return sharedPref.getString(key, "")
        }

        fun deleteData(context: Context, key: String) {
            val sharedPref = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                remove(key)
                apply()
            }
        }

    }
}