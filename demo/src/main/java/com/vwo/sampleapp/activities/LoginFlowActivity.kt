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
import androidx.appcompat.app.AppCompatActivity
import com.vwo.sampleapp.utils.RegistrationStore

class LoginFlowActivity : AppCompatActivity() {

    private val registrationStore = RegistrationStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val activity = if (registrationStore.isAlreadyRegistered()) {
            LoginActivity::class.java
        } else {
            RegisterActivity::class.java
        }*/
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
