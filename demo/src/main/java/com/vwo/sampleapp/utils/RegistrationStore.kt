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

const val key_username = "Username"
const val key_password = "Password"

class RegistrationStore {
    private val sPHelper = SharedPreferencesHelper

    fun save(userName: String, password: String) {
        sPHelper.saveString(key_username, userName)
        sPHelper.saveString(key_password, password)
    }

    fun isAlreadyRegistered(): Boolean {
        val userName = sPHelper.getString(key_username)
        val password = sPHelper.getString(key_password)
        if (userName.isBlank() || password.isBlank())
            return false
        return true
    }
}
