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

import android.text.TextUtils

object ApiKeyStore {

    private const val INSIGHTS_API_KEY = "insights_api_key"

    private const val INSIGHTS_ACC_ID = "insights_acc_id"

    private val sp = SharedPreferencesHelper

    @JvmStatic
    fun getAccountId() = sp.getString(INSIGHTS_ACC_ID)

    @JvmStatic
    fun saveAccountId(accountId: String) = sp.saveString(INSIGHTS_ACC_ID, accountId)

    @JvmStatic
    fun getApiKey() = sp.getString(INSIGHTS_API_KEY)

    @JvmStatic
    fun saveApiKey(accountId: String) = sp.saveString(INSIGHTS_API_KEY, accountId)

    @JvmStatic
    fun hasValidCredentials(): Boolean {
        return getApiKey().isNotBlank() && getAccountId().isNotBlank()
    }

}
