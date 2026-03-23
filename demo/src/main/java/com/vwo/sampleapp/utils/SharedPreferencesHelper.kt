package com.vwo.sampleapp.utils

/*
 * Copyright 2026 Wingify Software Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Copyright 2026 Wingify Software Pvt. Ltd.
 */
object SharedPreferencesHelper {
    private const val SHARED_PREFS_FILE = "sample_app_prefs"
    private const val API_KEY = "api_key"
    private var VWOapiKey: String? = null
    private var mSharedPreference: SharedPreferences? = null

    /**
     * Gets shared prefs.
     *
     * @param mContext the m context
     * @return the shared prefs
     */
    fun getSharedPrefs(mContext: Context): SharedPreferences? {
        if (mSharedPreference == null) {
            mSharedPreference =
                mContext.getSharedPreferences(mContext.packageName, Activity.MODE_PRIVATE)
        }
        return mSharedPreference
    }

    /**
     * Gets shared prefs.
     *
     * @param mContext the m context
     * @return the shared prefs
     */
    fun getVWOSharedPrefs(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences("VWO_shared_prefs", Activity.MODE_PRIVATE)
    }

    /**
     * Sets Api key.
     *
     * @param apiKey the application apiKey
     */
    @JvmStatic
    fun setApiKey(apiKey: String?, context: Context) {
        VWOapiKey = apiKey
        if (mSharedPreference == null) {
            getSharedPrefs(context)
        }
        mSharedPreference!!.edit().putString(API_KEY, apiKey).apply()
    }

    /**
     * Gets Api key.
     *
     */
    @JvmStatic
    fun getApiKey(context: Context): String? {
        if (mSharedPreference == null) {
            getSharedPrefs(context)
        }
        return mSharedPreference!!.getString(API_KEY, null)
    }

    fun removeApiKey(context: Context) {
        if (mSharedPreference == null) {
            getSharedPrefs(context)
        }
        mSharedPreference!!.edit().remove(API_KEY).apply()
    }

    fun getLong(key: String): Long {
        return mSharedPreference!!.getLong(key, 0)
    }

    fun saveLong(key: String, value: Long) {
        mSharedPreference!!.edit().apply {
            putLong(key, value)
            apply()
        }
    }

    fun getString(key: String): String {
        return mSharedPreference!!.getString(key, "") ?: ""
    }

    fun saveString(key: String, value: String) {
        mSharedPreference!!.edit().apply {
            putString(key, value)
            commit()
        }
    }

    fun clearDataForKey(key: String) {
        mSharedPreference!!.edit().apply {
            remove(key)
            apply()
        }
    }

    @JvmStatic
    fun clearData(context: Context) {
        getVWOSharedPrefs(context).edit().clear().apply()
    }

}
