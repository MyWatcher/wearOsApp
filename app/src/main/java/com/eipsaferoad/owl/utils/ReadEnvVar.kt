package com.eipsaferoad.owl.utils

import android.content.Context
import android.util.Log
import com.eipsaferoad.owl.R

class ReadEnvVar {

    enum class EnvVar(val str: String) {
        API_URL("API_URL")
    }

    companion object {
        fun readEnvVar(context: Context, name: EnvVar): String {
            if (vars.containsKey(name))
                return vars[name].toString()
            val data = context.resources.getString(R.string.api_url)
            vars[name] = data
            return data
        }
        private val vars: MutableMap<EnvVar, String> = mutableMapOf()
    }
}
