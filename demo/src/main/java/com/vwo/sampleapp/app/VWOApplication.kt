package com.vwo.sampleapp.app

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

import android.app.Application
import com.vwo.insights.VWOInsights
import com.vwo.insights.events.VWOLog
import com.vwo.insights.exposed.IVWOIntegrationCallback
import com.vwo.insights.exposed.IVwoInitCallback
import com.vwo.insights.exposed.Integrations
import com.vwo.insights.exposed.models.ClientConfiguration
import com.vwo.insights.screenshot.ScreenShotController
import com.vwo.sampleapp.utils.ApiKeyStore
import com.vwo.sampleapp.utils.RecordingStateManager
import com.vwo.sampleapp.utils.SharedPreferencesHelper
import timber.log.Timber
import timber.log.Timber.DebugTree


class VWOApplication : Application(), IVWOIntegrationCallback {
    private var screenShotController: ScreenShotController? = null

    var isSdkInitialized = false

    var sdkCallBack: IVwoInitCallback? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        RecordingStateManager.init(this)
        SharedPreferencesHelper.getSharedPrefs(this)

        val accountId = ApiKeyStore.getAccountId()
        val apiKey = ApiKeyStore.getApiKey()

        VWOLog.setLogLevel(VWOLog.ALL)
        // we don't need to init anything unless we have something saved in the shared preferences
        if (accountId.isNotBlank() && apiKey.isNotBlank()) {
            val configuration = ClientConfiguration(accountId, apiKey, userId = null)

            VWOInsights.init(this, object : IVwoInitCallback {
                override fun vwoInitSuccess(message: String) {
                    Timber.d("vwoInitSuccess: $message")
                    isSdkInitialized = true
                    VWOInsights.setCustomVariables(mapOf(Pair("sVariable", "SVlue")))
                    sdkCallBack?.vwoInitSuccess(message)
                }

                override fun vwoInitFailed(message: String) {
                    Timber.d("vwoInitFailed: $message")
                    isSdkInitialized = false
                    sdkCallBack?.vwoInitFailed(message)
                }
            }, configuration)
        }
    }

    override fun onVWOIntegrationCompleted(integrations: List<Integrations>) {
        // Integration callback - can be used for future integrations
    }
}
