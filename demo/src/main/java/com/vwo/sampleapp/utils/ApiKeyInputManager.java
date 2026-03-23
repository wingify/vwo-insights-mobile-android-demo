package com.vwo.sampleapp.utils;

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

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;
import com.vwo.sampleapp.R;
import com.vwo.sampleapp.activities.ScannerActivity;

public class ApiKeyInputManager {

    public static final String STR_EMPTY = "";

    public static final String COMMA = ",";

    public static final int API_KEY_LENGTH = 32;

    public static final int MAX_SPLIT_KEY_LENGTH = 2;

    public static final int STATUS_CODE = 0;

    public static ScanResult result = null;

    public static void showApiKeyDialog(final AppCompatActivity context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.popup_theme);
        builder.setTitle(context.getString(R.string.title_api_key));

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.input_dialog, null, false);
        // Set up the input
        final TextInputLayout textInputLayout = viewInflated.findViewById(R.id.input_wrapper);
        final AppCompatEditText input = textInputLayout.findViewById(R.id.input);
        final AppCompatEditText accountId = viewInflated.findViewById(R.id.account_id);

        if (!ApiKeyStore.getApiKey().isEmpty()) {
            String _apiKey = ApiKeyStore.getApiKey();
            String _accountId = ApiKeyStore.getAccountId();

            input.setText(_apiKey);
            accountId.setText(_accountId);
        }

        Button btnQrScanner = viewInflated.findViewById(R.id.btnQrScanner);
        btnQrScanner.setOnClickListener(v -> startScanner(input, accountId, v));
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError(STR_EMPTY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Set up the buttons
        builder.setPositiveButton("Save & Exit", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                Editable eInput = input.getText();
                Editable eAcc = accountId.getText();

                String apiKey = STR_EMPTY;
                String accId = STR_EMPTY;

                if (eInput != null && eAcc != null) {
                    apiKey = eInput.toString().trim();
                    accId = eAcc.toString().trim();
                }

                if (apiKey.isEmpty() || accId.isEmpty() || (apiKey.length() != API_KEY_LENGTH)) {

                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("You need to enter valid API key & Account ID.");
                } else {

                    ApiKeyStore.saveApiKey(apiKey);
                    ApiKeyStore.saveAccountId(accId);

                    alertDialog.dismiss();

                    // SHOW CONFIRMATION MESSAGE
                    AlertDialog.Builder _builder = new AlertDialog.Builder(context);
                    _builder.setCancelable(false);
                    _builder.setTitle("ATTENTION!!!");
                    _builder.setMessage("Please relaunch the application.");
                    _builder.setPositiveButton("Ok", (dialog1, which) -> {
                        killCurrentApplication(context);
                    });
                    _builder.create().show();
                }
            });
        });

        alertDialog.show();
    }

    private static void startScanner(AppCompatEditText input, AppCompatEditText accountId, View v) {
        result = result -> {
            if (result == null) return;

            String[] splitKeys = result.split(COMMA);
            if (splitKeys.length != MAX_SPLIT_KEY_LENGTH) return;

            input.setText(splitKeys[0].trim());
            accountId.setText(splitKeys[1].trim());
        };
        Intent intent = new Intent(v.getContext(), ScannerActivity.class);
        v.getContext().startActivity(intent);
    }

    private static void killCurrentApplication(Activity activity) {
        clearCurrentActivities(activity);
        System.exit(STATUS_CODE);
    }

    private static void clearCurrentActivities(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
        activity.finishAffinity();
    }
}
