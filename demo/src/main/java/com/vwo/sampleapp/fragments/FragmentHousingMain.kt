package com.vwo.sampleapp.fragments

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

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.vwo.sampleapp.R
import com.vwo.sampleapp.activities.fromFragments.HousingActivity
import com.vwo.sampleapp.databinding.FragmentHousingMainBinding
import com.vwo.sampleapp.interfaces.ChangeFragment
import com.vwo.sampleapp.interfaces.NavigationToggleListener

/**
 * Copyright 2026 Wingify Software Pvt. Ltd.
 */

class FragmentHousingMain : Fragment(), ChangeFragment {

    private var listener: NavigationToggleListener? = null

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(VARIATION_LOGIN_TYPE_NORMAL)
    internal annotation class LoginType

    /*override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is NavigationToggleListener) {
            listener = context
        } else {
            Log.e(LOG_TAG, "Interface NavigationToggleListener not implemented in Activity")
        }
    }*/

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = FragmentHousingMainBinding.inflate(inflater, container, false)

        val navigation = view.toolbarCommon.campaignNavigation
        val refresh = view.toolbarCommon.refreshCampaign
        val toolbarTitle = view.toolbarCommon.toolbarTitle

        navigation.setOnClickListener {
            if (listener != null) {
                listener!!.onToggle()
            }
        }

        refresh.visibility = View.GONE

        toolbarTitle.setText(R.string.title_variable_campaign)
        loadDefaultFragments()

        if (requireActivity() is HousingActivity) {
            val menu: ImageView = view.toolbarCommon.campaignNavigation
            menu.visibility = View.GONE

            val back: ImageView = view.toolbarCommon.campaignBack
            back.visibility = View.VISIBLE
            back.setOnClickListener {
                requireActivity().finish()
            }
        } else {
            val menu: ImageView = view.toolbarCommon.campaignNavigation
            menu.visibility = View.VISIBLE

            val back: ImageView = view.toolbarCommon.campaignBack
            back.visibility = View.GONE
        }
        return view.root
    }

    private fun loadDefaultFragments() {
        loadFragment(null, VARIATION_LOGIN_TYPE_NORMAL, null)
    }

    /**
     * ** This function is used to load a particular [android.app.Fragment] from the
     * controlling [Activity] or [android.app.Fragment] **
     *
     * @param bundle     is the data to be passed to [android.app.Fragment]
     * @param fragmentId is the id that identifies, which [android.app.Fragment] is to be loaded
     * @param tag        is the tag that is attached to [android.app.Fragment] which is to be loaded
     */
    override fun loadFragment(@Nullable bundle: Bundle?, fragmentId: Int, @Nullable tag: String?) {
        childFragmentManager.beginTransaction().replace(R.id.onboarding_variation_container,
                FragmentHousing.getInstance(fragmentId)).commit()
    }

    companion object {
        private val LOG_TAG = FragmentHousingMain::class.java.simpleName

        const val VARIATION_LOGIN_TYPE_NORMAL = 2
    }
}
