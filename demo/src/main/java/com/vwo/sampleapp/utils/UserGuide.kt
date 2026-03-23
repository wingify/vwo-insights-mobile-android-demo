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
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PointF
import android.os.Handler
import android.os.Looper
import android.view.View
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.vwo.sampleapp.R
import com.vwo.sampleapp.activities.LoginActivity
import com.vwo.sampleapp.activities.MainActivity
import com.vwo.sampleapp.activities.ProfileActivity
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal


/**
/* Copyright 2026 Wingify Software Pvt. Ltd. */
 **/
@SuppressLint("ApplySharedPref")
object UserGuide {

    private const val TAP_TARGET_SHOW_DELAY = 100L

    private const val STATIC_STATUS_BAR_SHIFT_SIZE = 60

    private const val TAP_TARGET_SIZE_DP = 40f

    private const val COLOR_TRANSPARENT = "#00000000"

    private const val PREF_FILE_NAME = "_tap_target"

    private const val KEY_SESSION_RECORDING_START = "is_shown_for_start_session_recording"

    private const val KEY_INFO_SHOWN_AFTER_SESSION_START = "is_shown_after_session_recording_start"

    private const val KEY_MENU_POPUP_SHOWN = "is_shown_menu_popup"

    private const val KEY_PROFILE_MENU_POPUP_SHOWN = "is_shown_profile_menu_popup"

    private const val KEY_STOP_SESSION_MENU_POPUP_SHOWN = "is_shown_stop_session_menu_popup"

    private const val KEY_MENU_ANR_SHOWN = "is_shown_anr_popup"

    private const val KEY_MENU_CRASH_SHOWN = "is_shown_crash_popup"

    private const val KEY_PHONE_NUMBER_SHOWN = "is_phone_number_shown"

    private const val KEY_EMAIL_SHOWN = "is_email_shown"

    @JvmStatic
    fun showForEnterApiKey(activity: LoginActivity) {

        // don't show anything if there is Account ID and API Key
        if (ApiKeyStore.hasValidCredentials()) return

        showPopup(
            activity = activity,
            title = "API Key",
            message = "Please click here to add Account ID and API Key.",
            id = R.id.btn_api_key,
        )
    }

    @JvmStatic
    fun isAllTapTargetShownForProfile(context: Activity): Boolean {
        val sp = getSharedPreferences(context)
        return sp.contains(KEY_MENU_ANR_SHOWN) && sp.contains(KEY_MENU_CRASH_SHOWN) && sp.contains(
            KEY_PHONE_NUMBER_SHOWN
        ) && sp.contains(KEY_EMAIL_SHOWN)
    }

    @JvmStatic
    fun isAllTapTargetNotShownForProfile(context: Activity) =
        !isAllTapTargetShownForProfile(context)

    @JvmStatic
    fun showForAnrPhoneEmailAndCrash(activity: ProfileActivity) {

        val sp = getSharedPreferences(activity)

        val anrListener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
            if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                sp.edit().putBoolean(KEY_MENU_ANR_SHOWN, true).commit()
            }
        }
        val anr = MaterialTapTargetPrompt.Builder(activity).setTarget(R.id.btn_anr)
            .setPrimaryText("Application Not Responding ( A.N.R )")
            .setPromptFocal(RectanglePromptFocal())
            .setSecondaryText("Click here to trigger A.N.R event.")
            .setBackButtonDismissEnabled(false).setPromptStateChangeListener(anrListener).create()

        val crashListener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
            if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                sp.edit().putBoolean(KEY_MENU_CRASH_SHOWN, true).commit()
            }
        }
        val crash = MaterialTapTargetPrompt.Builder(activity).setTarget(R.id.btn_crash)
            .setPrimaryText("Crash").setPromptFocal(RectanglePromptFocal())
            .setSecondaryText("Click here to trigger a crash event.")
            .setBackButtonDismissEnabled(false).setPromptStateChangeListener(crashListener).create()

        val maskPhoneListener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
            if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                sp.edit().putBoolean(KEY_PHONE_NUMBER_SHOWN, true).commit()
            }
        }
        val maskPhone = MaterialTapTargetPrompt.Builder(activity).setTarget(R.id.mask_ph_wrapper)
            .setPrimaryText("Phone Number").setPromptFocal(RectanglePromptFocal())
            .setSecondaryText("This PHONE NUMBER will be masked in the session recordings. Please add tag filter with name : demo_mask")
            .setBackButtonDismissEnabled(false).setPromptStateChangeListener(maskPhoneListener)
            .create()

        val maskEmailListener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
            if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                sp.edit().putBoolean(KEY_EMAIL_SHOWN, true).commit()
            }
        }
        val maskEmail = MaterialTapTargetPrompt.Builder(activity).setTarget(R.id.mask_email_wrapper)
            .setPrimaryText("Email").setPromptFocal(RectanglePromptFocal())
            .setSecondaryText("This EMAIL will be masked in the session recordings. Please add tag filter with name : demo_mask")
            .setBackButtonDismissEnabled(false).setPromptStateChangeListener(maskEmailListener)
            .create()

        MaterialTapTargetSequence().apply {

            if (!sp.contains(KEY_MENU_ANR_SHOWN)) {
                addPrompt(anr)
            }

            if (!sp.contains(KEY_MENU_CRASH_SHOWN)) {
                addPrompt(crash)
            }

            /*if (!sp.contains(KEY_PHONE_NUMBER_SHOWN)) {
                addPrompt(maskPhone)
            }

            if (!sp.contains(KEY_EMAIL_SHOWN)) {
                addPrompt(maskEmail)
            }*/

        }.show()
    }

    @JvmStatic
    fun showLoginScreenSessionRecordingHelp(activity: LoginActivity, listener: () -> Unit) {

        if (!ApiKeyStore.hasValidCredentials()) return // no point in showing as button is not visible

        val sp = getSharedPreferences(activity)
        val listener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
            if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                sp.edit().putBoolean(KEY_SESSION_RECORDING_START, true).commit()
                listener.invoke()
            }
        }

        if (sp.contains(KEY_SESSION_RECORDING_START)) return // already shown no need to show

        showPopup(
            activity = activity,
            title = "Start Session Recording",
            message = "You can click here to start session recording",
            id = R.id.btn_start_recording,
            listener = listener
        )
    }

    @JvmStatic
    fun showMainActivityInfo(activity: MainActivity) {

        val sp = getSharedPreferences(activity)
        if (sp.contains(KEY_MENU_POPUP_SHOWN)) return

        Handler(Looper.getMainLooper()).postDelayed({
            val listener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    sp.edit().putBoolean(KEY_MENU_POPUP_SHOWN, true).commit()
                }
            }

            showPopup(
                activity = activity,
                title = "Menu",
                message = "Click here to navigate around.",
                id = R.id.campaign_navigation,
                listener = listener
            )
        }, TAP_TARGET_SHOW_DELAY)
    }

    @JvmStatic
    fun automaticallySelectBetweenProfileAndStopSession(activity: MainActivity) {
        val isShown: Boolean = getSharedPreferences(activity).contains(KEY_PROFILE_MENU_POPUP_SHOWN)
        if (!isShown) {
            showOnProfile(activity)
        } else {
            showOnStopSessionRecording(activity)
        }
    }

    private fun showOnStopSessionRecording(activity: MainActivity) {

        val sp = getSharedPreferences(activity)
        if (sp.contains(KEY_STOP_SESSION_MENU_POPUP_SHOWN)) return

        Handler(Looper.getMainLooper()).postDelayed({

            // THIS IS A HACKY APPROACH AS WE CANNOT RELIABLY SHOW THIS POPUP IN NAVIGATION MENUS

            // the menu view to extract it's location
            val int = IntArray(2)
            val view = activity.findViewById<View>(R.id.nav_stop_session)
            view.getLocationOnScreen(int)

            val mX = int[0].toFloat()
            // adding it for the status bar hack, must shift this amount
            val mY = int[1].toFloat() + STATIC_STATUS_BAR_SHIFT_SIZE

            val listener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    sp.edit().putBoolean(KEY_STOP_SESSION_MENU_POPUP_SHOWN, true).commit()
                }
            }

            MaterialTapTargetPrompt.Builder(activity).setTarget(mX, mY)
                .setPrimaryText("Stop Session Recording")
                .setFocalColour(Color.parseColor(COLOR_TRANSPARENT))
                .setPromptFocal(RectanglePromptFocal().apply {
                    setSize(
                        PointF(
                            getScreenWidth(activity), pxFromDp(activity, TAP_TARGET_SIZE_DP)
                        )
                    )
                }).setSecondaryText("Click here to stop the session recording that is in progress.")
                .setBackButtonDismissEnabled(false).setPromptStateChangeListener(listener).show()

        }, TAP_TARGET_SHOW_DELAY)
    }

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }


    private fun getScreenWidth(activity: MainActivity): Float {
        return activity.resources.displayMetrics.widthPixels.toFloat()
    }

    @JvmStatic
    fun showOnProfile(activity: MainActivity) {

        val sp = getSharedPreferences(activity)
        if (sp.contains(KEY_PROFILE_MENU_POPUP_SHOWN)) return

        Handler(Looper.getMainLooper()).postDelayed({

            // THIS IS A HACKY APPROACH AS WE CANNOT RELIABLY SHOW THIS POPUP IN NAVIGATION MENUS

            // the menu view to extract it's location
            val int = IntArray(2)
            val view = activity.findViewById<View>(R.id.nav_profile)
            view.getLocationOnScreen(int)

            val mX = int[0].toFloat()
            // adding it for the status bar hack, must shift this amount
            val mY = int[1].toFloat() + STATIC_STATUS_BAR_SHIFT_SIZE

            val listener = MaterialTapTargetPrompt.PromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FINISHED || state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                    sp.edit().putBoolean(KEY_PROFILE_MENU_POPUP_SHOWN, true).commit()
                }
            }

            MaterialTapTargetPrompt.Builder(activity).setTarget(mX, mY).setPrimaryText("Profile")
                .setPromptFocal(RectanglePromptFocal().apply {
                    val dp = pxFromDp(activity, TAP_TARGET_SIZE_DP)
                    setSize(PointF(getScreenWidth(activity), dp))
                }).setFocalColour(Color.parseColor(COLOR_TRANSPARENT))
                .setSecondaryText("You can see the profile view which has multiple insights options.")
                .setBackButtonDismissEnabled(false).setPromptStateChangeListener(listener).show()

        }, TAP_TARGET_SHOW_DELAY)
    }

    @JvmStatic
    fun showInfoAfterRecordingStarts(activity: LoginActivity, listener: () -> Unit) {
        // show popup mentioning that everything the app is capable of
        BubbleShowCaseBuilder(activity).title("Great!")
            .description("The recording has now started. You can now perform events like 'Single Tap' , 'Double Tap' , 'Scroll'. Please click here to continue.")
            .targetView(activity.findViewById(R.id.loginButton))
            .listener(object : BubbleShowCaseListener {

                private fun updateValue() {
                    getSharedPreferences(activity).edit()
                        .putBoolean(KEY_INFO_SHOWN_AFTER_SESSION_START, true).commit()
                }

                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {
                    updateValue()
                }

                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {
                    updateValue()
                }

                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {
                    updateValue()
                    listener.invoke()
                }

                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    updateValue()
                    listener.invoke()
                }

            }).show()
    }

    private fun getSharedPreferences(activity: Activity): SharedPreferences {
        return activity.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    private fun showPopup(
        activity: Activity,
        title: String,
        message: String,
        id: Int,
        listener: MaterialTapTargetPrompt.PromptStateChangeListener? = null
    ) {
        MaterialTapTargetPrompt.Builder(activity).setTarget(activity.findViewById(id))
            .setPrimaryText(title).setClipToView(activity.window.decorView.rootView)
            .setPromptFocal(RectanglePromptFocal()).setSecondaryText(message)
            .setFocalColour(Color.parseColor(COLOR_TRANSPARENT)).setBackButtonDismissEnabled(false)
            .setPromptStateChangeListener(listener).show()
    }

}
