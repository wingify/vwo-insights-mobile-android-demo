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

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class RecordingStateManager(
    private val context: Application
) {

    companion object {

        private const val KEY_IS_RECORDING_STARTED = "is_recording_started"

        private lateinit var INSTANCE: RecordingStateManager

        fun init(context: Application) {
            INSTANCE = RecordingStateManager(context)
        }

        fun instance() = INSTANCE
    }

    private fun getSharedPref(): SharedPreferences {
        return context.getSharedPreferences("states", Context.MODE_PRIVATE)
    }

    fun isRecordingStarted(): Boolean {
        return getSharedPref().getBoolean(KEY_IS_RECORDING_STARTED, false)
    }

    fun isRecordingStopped(): Boolean {
        return !isRecordingStarted()
    }

    @SuppressLint("ApplySharedPref")
    fun toggleState() {
        getSharedPref().edit().putBoolean(KEY_IS_RECORDING_STARTED, !isRecordingStarted()).commit()
    }

}
