package com.eipsaferoad.owl.utils

import android.content.Context
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

enum class EnvEnum(val value: String) {
    EMAIL("email"),
    PASSWORD("password"),
    ALARM("alarm"),
    VIBRATION_ALARM("vibration"),
    SOUND_ALARM("sound"),
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