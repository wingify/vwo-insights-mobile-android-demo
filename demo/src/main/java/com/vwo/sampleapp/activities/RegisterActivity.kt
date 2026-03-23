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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vwo.sampleapp.R
import com.vwo.sampleapp.databinding.ActivityRegistrationBinding
import com.vwo.sampleapp.utils.RegistrationStore

class RegisterActivity : AppCompatActivity() {

    private val registrationStore = RegistrationStore()
    private var binding: ActivityRegistrationBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_registration)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onRegister(view: View) {
        val userName = binding?.emailEditText?.text.toString()
        val password = binding?.passwordEditText?.text.toString()
        val confirmPass = binding?.passwordConfirmEditText?.text.toString()

        if (isDataValid(userName, password, confirmPass)) {
            registrationStore.save(userName, password)
            val loggedIn = Intent(this, LoginActivity::class.java)
            startActivity(loggedIn)
            finish()
        }
    }

    private fun isDataValid(userName: String, password: String, confirmPass: String): Boolean {

        if (userName.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            showDialog("Username, Password and Confirm Password fields can not be empty!")
            return false
        }
        if (password != confirmPass) {
            showDialog("Password and Confirm Password do not match!")
            return false
        }
        return true
    }

    fun signIn(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun showDialog(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setIcon(R.mipmap.ic_launcher)
            .setMessage(message)
            .setTitle("Error")
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }
}
