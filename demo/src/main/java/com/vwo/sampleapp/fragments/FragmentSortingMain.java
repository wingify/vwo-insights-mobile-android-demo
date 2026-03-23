package com.vwo.sampleapp.fragments;

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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vwo.sampleapp.R;
import com.vwo.sampleapp.activities.SettingsActivity;
import com.vwo.sampleapp.activities.fromFragments.SortingActivity;
import com.vwo.sampleapp.interfaces.ChangeFragment;
import com.vwo.sampleapp.interfaces.NavigationToggleListener;
import com.vwo.sampleapp.models.Mobile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class FragmentSortingMain extends Fragment implements ChangeFragment {
    private static final String LOG_TAG = FragmentSortingMain.class.getSimpleName();

    private NavigationToggleListener listener;

    public static final String TAG_CONTROL = "Control";
    public static final String TAG_VARIATION = "Variation";

    public static final String CURRENT_FRAGMENT_ID = "current_fragment_id";

    public void refreshChildFragments() {
        if (currentFragmentID == ID_LIST_VARIATION) {
            FragmentSorting fragment = (FragmentSorting) getChildFragmentManager().findFragmentByTag(TAG_VARIATION);
            if (fragment != null && fragment.isVisible()) {
                fragment.onRefreshClicked();
            } else {
                loadFragments();
            }
        } else {
            loadFragments();
        }
    }

    private void setToolbarText(String text) {
        toolbarTitle.setText(text);
    }

    public void resetToolbarText() {
        setToolbarText(getString(R.string.title_layout_campaign));
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            ID_LIST_VARIATION,
            ID_DETAILS_VARIATION})
    public @interface FragmentType {
    }

    public static final int ID_LIST_VARIATION = 1;
    public static final int ID_DETAILS_VARIATION = 2;

    private int currentFragmentID = -1;

    private AppCompatTextView toolbarTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NavigationToggleListener) {
            listener = (NavigationToggleListener) context;
        } else {
            Log.e(LOG_TAG, "Interface NavigationToggleListener not implemented in Activity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_main, container, false);
        setHasOptionsMenu(true);

        AppCompatImageView navigation = view.findViewById(R.id.campaign_navigation);
        AppCompatImageView refresh = view.findViewById(R.id.refresh_campaign);
        AppCompatImageView ivSettings = view.findViewById(R.id.iv_settings);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        toolbarTitle.setText(R.string.title_layout_campaign);
        toolbarTitle.setTag(com.vwo.insights.R.id.hideViewId, "Type A");

        if (savedInstanceState != null) {
            currentFragmentID = savedInstanceState.getInt(CURRENT_FRAGMENT_ID, ID_LIST_VARIATION);
        } else {
            currentFragmentID = ID_LIST_VARIATION;
        }

        navigation.setOnClickListener(view1 -> {
            if (listener != null) {
                listener.onToggle();
            }
        });

        loadFragments();

        refresh.setOnClickListener(view12 -> {
            refreshChildFragments();
        });
        ivSettings.setOnClickListener(settingsView -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        });

        ImageView back = view.findViewById(R.id.campaign_back);
        if (requireActivity() instanceof SortingActivity) {

            navigation.setVisibility(View.GONE);

            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().finish();
                }
            });
        } else {

            navigation.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        }

        return view;
    }

    private void loadFragments() {
        loadFragment(null, currentFragmentID, null);
    }

    /**
     * <b> This function is used to load a particular {@link Fragment} from the
     * controlling {@link Activity} or {@link .Fragment} </b>
     *
     * @param bundle     is the data to be passed to {@link Fragment}
     * @param fragmentId is the id that identifies, which {@link Fragment} is to be loaded
     * @param tag        is the tag that is attached to {@link Fragment} which is to be loaded
     */
    @Override
    public void loadFragment(@Nullable Bundle bundle, int fragmentId, @Nullable String tag) {
        switch (fragmentId) {
            case ID_LIST_VARIATION:
                Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    getChildFragmentManager().beginTransaction().replace(R.id.sorting_variation_container,
                            FragmentSorting.getInstance(fragmentId), TAG_VARIATION).commit();
                } else {
                    getChildFragmentManager().beginTransaction().replace(R.id.sorting_variation_container,
                            fragment, tag).commit();
                }
                currentFragmentID = fragmentId;
                break;
            case ID_DETAILS_VARIATION:
                if (bundle != null) {
                    Mobile mobile = bundle.getParcelable(FragmentSorting.ARG_ITEM);
                    toolbarTitle.setText(mobile.getName());
                    FragmentItemDetails detailsFragment = FragmentItemDetails.getInstance(mobile, fragmentId);
                    getChildFragmentManager().beginTransaction().replace(R.id.sorting_variation_container,
                            detailsFragment, null).addToBackStack(null).commit();
                }
                currentFragmentID = fragmentId;
                break;
            default:
                throw new IllegalArgumentException("Unknown fragment id : " + fragmentId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT_ID, currentFragmentID);
        super.onSaveInstanceState(outState);
    }
}
