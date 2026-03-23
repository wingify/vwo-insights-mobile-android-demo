package com.vwo.sampleapp.activities;

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

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.vwo.insights.VWOInsights;
import com.vwo.insights.exposed.IVwoInitCallback;
import com.vwo.sampleapp.R;
import com.vwo.sampleapp.activities.fromFragments.HousingActivity;
import com.vwo.sampleapp.activities.fromFragments.SortingActivity;
import com.vwo.sampleapp.app.VWOApplication;
import com.vwo.sampleapp.fragments.FragmentHousingMain;
import com.vwo.sampleapp.fragments.FragmentSortingMain;
import com.vwo.sampleapp.interfaces.NavigationToggleListener;
import com.vwo.sampleapp.utils.ApiKeyInputManager;
import com.vwo.sampleapp.utils.ApiKeyStore;
import com.vwo.sampleapp.utils.RecordingStateManager;
import com.vwo.sampleapp.utils.SharedPreferencesHelper;
import com.vwo.sampleapp.utils.UserGuide;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationToggleListener, IVwoInitCallback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String TAG_HOUSING = "housing";
    private static final String TAG_SORTING = "sorting";

    private static final int ID_FRAGMENT_SORTING = 0;
    private static final int ID_FRAGMENT_HOUSING = 1;
    private ProgressBar progressBar;
    private NavigationView navigationView;

    private MenuItem stopSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_layout_campaign);

        stopSession = (MenuItem) navigationView.getMenu().findItem(R.id.nav_stop_session);
        updateStopState();

        progressBar = findViewById(R.id.loading_progress);
        Intent intent = getIntent();

        Uri data = intent.getData();
        if (data != null) {
            if (data.getPathSegments().size() == 2) {
                String apiKey = data.getPathSegments().get(1);
                if (!TextUtils.isEmpty(apiKey)) {

                    Log.d(LOG_TAG, "API_KEY: " + apiKey);

                    // Do something with idString
                    if (validateAndSetApiKey(apiKey)) {
                        initVWO(apiKey, false);
                    }
                } else {
                    initVWO(SharedPreferencesHelper.getApiKey(this), false);
                }
            }
        } else {
            initVWO(SharedPreferencesHelper.getApiKey(this), false);
        }

        UserGuide.INSTANCE.showMainActivityInfo(MainActivity.this);
    }

    private void updateStopState() {
        stopSession.setEnabled(RecordingStateManager.Companion.instance().isRecordingStarted());
        stopSession.setChecked(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        VWOApplication vwoApplication = (VWOApplication) getApplication();
        vwoApplication.setSdkCallBack(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    AlertDialog ad = null;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_layout_campaign) {
            // loadFragment(null, ID_FRAGMENT_SORTING, TAG_SORTING);
            Intent intent = new Intent(this, SortingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_onboarding_campaign) {
            // loadFragment(null, ID_FRAGMENT_HOUSING, TAG_HOUSING);
            Intent intent = new Intent(this, HousingActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_clear_data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.popup_theme);
            builder.setTitle(getString(R.string.confirm));
            builder.setMessage(getString(R.string.clear_data_message));
            builder.setNegativeButton(R.string.clear_data_negative, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton(R.string.clear_data_positive, (dialogInterface, i) -> {
                SharedPreferencesHelper.clearData(MainActivity.this);
                dialogInterface.dismiss();
                Toast.makeText(MainActivity.this, getString(R.string.data_cleared), Toast.LENGTH_SHORT).show();
            });
            builder.show();
        } else if (id == R.id.action_enter_api_key) {
            ApiKeyInputManager.showApiKeyDialog(this);
        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(MainActivity.this, CartAndFavoriteActivity.class);
            intent.putExtra(CartAndFavoriteActivity.KEY_TYPE, CartAndFavoriteActivity.TYPE_CART);
            startActivity(intent);
        } else if (id == R.id.nav_favorite) {

            Intent intent = new Intent(MainActivity.this, CartAndFavoriteActivity.class);
            intent.putExtra(CartAndFavoriteActivity.KEY_TYPE, CartAndFavoriteActivity.TYPE_FAVORITE);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stop_session) {
            showStopSessionRecordingDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showStopSessionRecordingDialog() {
        boolean isRecordingOff = !RecordingStateManager.Companion.instance().isRecordingStarted();
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Attention");
        if (isRecordingOff) {
            adb.setMessage("Session recording has not yet started.");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ad.dismiss();
                }
            });
        } else {
            adb.setMessage("Do you really want to stop session recording?");
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RecordingStateManager.Companion.instance().toggleState();
                    VWOInsights.stopSessionRecording();
                    updateStopState();
                    ad.dismiss();
                }
            });
            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ad.dismiss();
                }
            });
        }

        ad = adb.create();
        ad.show();
    }

    @CheckResult
    private boolean validateAndSetApiKey(String apiKey) {
        Toast.makeText(MainActivity.this, getString(R.string.api_key_set), Toast.LENGTH_SHORT).show();
        ApiKeyStore.saveApiKey(apiKey);
        return true;
    }

    private void initVWO(String key, final boolean showProgress) {
        Log.d("INIT", "Calling initVWO");
//        if (!TextUtils.isEmpty(key)) {

        /*VWOApplication vwoApplication = (VWOApplication) getApplication();
        if (vwoApplication.isSdkInitialized())
            VWOInsights.stopSessionRecording();*/

        // vwoApplication.setSdkCallBack(this);
        loadFragments();

//        } else {
//            loadFragments();
//        }
    }

    private void loadFragments() {
        int fragmentID = getCurrentFragmentID();
        if (fragmentID == ID_FRAGMENT_HOUSING) {
            new Handler(getMainLooper()).post(() -> loadFragment(null, ID_FRAGMENT_HOUSING, TAG_HOUSING));
        } else {
            new Handler(getMainLooper()).post(() -> loadFragment(null, ID_FRAGMENT_SORTING, TAG_SORTING));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void loadFragment(@Nullable Bundle bundle, int fragmentId, @Nullable String tag) {
        Log.d("FragmentID", "Loading fragment with id : " + fragmentId);
        switch (fragmentId) {
            case ID_FRAGMENT_SORTING:
                if (getCurrentFragmentID() != fragmentId) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new FragmentSortingMain(), tag).commit();
                }
                navigationView.setCheckedItem(R.id.nav_layout_campaign);
                break;
            case ID_FRAGMENT_HOUSING:
                if (getCurrentFragmentID() != fragmentId) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new FragmentHousingMain(), tag).commit();
                }
                navigationView.setCheckedItem(R.id.nav_onboarding_campaign);
                break;
        }
        super.loadFragment(bundle, fragmentId, tag);
    }

    @Override
    public void onToggle() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);

            // drawer opened
            UserGuide.automaticallySelectBetweenProfileAndStopSession(this);
        }
    }

    @Override
    public void vwoInitSuccess(@NonNull String s) {
        VWOInsights vwoInsights = VWOInsights.INSTANCE;
        vwoInsights.startSessionRecording();
    }

    @Override
    public void vwoInitFailed(@NonNull String s) {
    }
}
