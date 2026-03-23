package com.vwo.sampleapp.activities

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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.vwo.insights.VWOInsights
import com.vwo.insights.exposed.IVwoInitCallback
import com.vwo.sampleapp.R
import com.vwo.sampleapp.app.VWOApplication
import com.vwo.sampleapp.utils.ApiKeyInputManager
import com.vwo.sampleapp.utils.ApiKeyStore
import com.vwo.sampleapp.utils.RecordingStateManager
import com.vwo.sampleapp.utils.UserGuide
import timber.log.Timber

class LoginActivity : AppCompatActivity(), IVwoInitCallback {

    private val btnStartRecording: Button by lazy { findViewById(R.id.btn_start_recording) }
    private val login: Button by lazy { findViewById(R.id.loginButton) }
    private val scrollView: NestedScrollView by lazy { findViewById(R.id.rootView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.post {
            scrollView.scrollTo(0, login.bottom)
            UserGuide.showForEnterApiKey(this)
            UserGuide.showLoginScreenSessionRecordingHelp(this) {}
        }

        (application as VWOApplication).sdkCallBack = this

        login.setTag(com.vwo.insights.R.id.hideViewId, "my_custom_tag")

        val map = HashMap<String, String>()
        map["key1"] = "value1"
        map["key2"] = "value2"
        VWOInsights.sendCustomEvent("custom_event_1", map)
        login.setOnClickListener {
            val loggedIn = Intent(this, MainActivity::class.java)
            startActivity(loggedIn)
        }

        val btnChangeApiKey: Button = findViewById(R.id.btn_api_key)
        btnChangeApiKey.setOnClickListener {
            ApiKeyInputManager.showApiKeyDialog(this)
        }

        btnStartRecording.visibility =
            if (ApiKeyStore.hasValidCredentials()) View.VISIBLE else View.INVISIBLE
        btnStartRecording.setOnClickListener {
            startOrStopSessionRecording()
            updateLoginButtonState(login)
        }

        updateLoginButtonState(login)
        updateUiStatesForButtons()
    }

    override fun onResume() {
        super.onResume()
        updateLoginButtonState(login)
        updateUiStatesForButtons()
    }

    private fun updateUiStatesForButtons() {
        // start recording if user already started the recording
        if (ApiKeyStore.hasValidCredentials()
            && RecordingStateManager.instance().isRecordingStarted()
        ) {
            btnStartRecording.alpha = 0.4f
            btnStartRecording.isEnabled = false
            btnStartRecording.text = getString(R.string.stop_recording)
        } else {
            Timber.e("will not start recording because of unmet conditions")
            btnStartRecording.text = getString(R.string.start_recording)
        }
    }

    private fun updateLoginButtonState(login: Button) {
        // if user has saved credentials
        login.isEnabled = ApiKeyStore.hasValidCredentials()
                && RecordingStateManager.instance().isRecordingStarted()
        if (!login.isEnabled) {
            login.alpha = 0.4f
        } else {
            login.alpha = 1.0f
        }
    }

    private fun startOrStopSessionRecording() {
        Timber.d("user pressed ${btnStartRecording.text}")
        val state0 = RecordingStateManager.instance().isRecordingStarted()
        Timber.d("before state update : isRecording -> $state0")
        RecordingStateManager.instance().toggleState()
        val state1 = RecordingStateManager.instance().isRecordingStarted()
        Timber.d("after state update : isRecording -> $state1")
        if (RecordingStateManager.instance().isRecordingStarted()) {
            Timber.d("START recording now.")
            try {
                VWOInsights.startSessionRecording()
                btnStartRecording.text = getString(R.string.stop_recording)
                UserGuide.showInfoAfterRecordingStarts(this@LoginActivity) {
                    login.callOnClick()
                }
            } catch (ex: Exception) {
                // exception when VWO not initialized
                RecordingStateManager.instance().toggleState()
            }
        } else {
            Timber.d("STOP recording now.")
            VWOInsights.stopSessionRecording()
            btnStartRecording.text = getString(R.string.start_recording)
            Toast.makeText(this, "Session recording stopped.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun vwoInitSuccess(message: String) {
        if (ApiKeyStore.hasValidCredentials()
            && RecordingStateManager.instance().isRecordingStarted()
        ) {
            VWOInsights.startSessionRecording()
            Timber.d("recording started automatically because conditions were satisfied")
            btnStartRecording.isEnabled = true
            btnStartRecording.alpha = 1.0f
        }
    }

    override fun vwoInitFailed(message: String) {
    }

    fun signUp(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

}
