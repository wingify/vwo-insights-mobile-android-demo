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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.vwo.sampleapp.R;
import com.vwo.sampleapp.data.MobileViewModel;
import com.vwo.sampleapp.interfaces.ChangeFragment;
import com.vwo.sampleapp.interfaces.ItemClickListener;
import com.vwo.sampleapp.models.Mobile;

import java.util.List;

public class FragmentSorting extends Fragment implements ItemClickListener {

    private static final String LOG_TAG = FragmentSorting.class.getSimpleName();

    static final String ARG_ITEM = "item";
    private static final String ARG_FRAGMENT_TYPE = "fragment_type";

    private int type;

    private MobileViewModel mobileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting, container, false);

        NestedScrollView nestedScrollView = view.findViewById(R.id.sorting_nested_view);

        if (savedInstanceState == null) {
            assert getArguments() != null;
            type = getArguments().getInt(ARG_FRAGMENT_TYPE, FragmentSortingMain.ID_LIST_VARIATION);
        } else {
            type = savedInstanceState.getInt(ARG_FRAGMENT_TYPE, FragmentSortingMain.ID_LIST_VARIATION);
        }

        mobileViewModel = ViewModelProviders.of(this).get(MobileViewModel.class);

        mobileViewModel.getMobiles().observe(this, mobiles -> {
            parseForEachMobile(nestedScrollView, mobiles);
        });

        return view;
    }

    private void parseForEachMobile(NestedScrollView nestedScrollView, List<Mobile> mobiles) {
        // need to remove previous views because scroll view can only have one direct child
        nestedScrollView.removeAllViews();

        // update the scroll view as we get the data of mobile here
        LinearLayout root = new LinearLayout(requireActivity());
        root.setOrientation(LinearLayout.VERTICAL);
        for (Mobile mobile : mobiles) {
            prepareUiForEachMobileItem(mobile, root);
        }
        nestedScrollView.addView(root);
    }

    private void prepareUiForEachMobileItem(Mobile mobile, LinearLayout root) {
        View item = getLayoutInflater().inflate(R.layout.single_item_sorting_list, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(24, 24, 24, 24);
        item.setLayoutParams(lp);
        root.addView(item);

        // update data
        TextView title = item.findViewById(R.id.item_name);
        title.setText(mobile.getName());
        TextView vendor = item.findViewById(R.id.item_vendor);
        vendor.setText(mobile.getVendor());
        TextView price = item.findViewById(R.id.item_price);
        price.setText(String.format(getString(R.string.price_format), mobile.getUnits(), mobile.getPrice()));
        ImageView image = item.findViewById(R.id.item_image);
        image.setImageResource(mobile.getImageId());

        item.setOnClickListener(v -> {
            if (getParentFragment() instanceof ChangeFragment) {
                ChangeFragment listener = (ChangeFragment) getParentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_ITEM, mobile);
                listener.loadFragment(bundle, FragmentSortingMain.ID_DETAILS_VARIATION, FragmentSortingMain.TAG_VARIATION);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshVariation();
    }

    private void refreshVariation() {
        mobileViewModel.sortById();
    }

    static FragmentSorting getInstance(@FragmentSortingMain.FragmentType int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_FRAGMENT_TYPE, type);

        FragmentSorting fragmentSorting = new FragmentSorting();
        fragmentSorting.setArguments(bundle);

        return fragmentSorting;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_FRAGMENT_TYPE, type);
    }

    @Override
    public void onItemClicked(RecyclerView.ViewHolder viewHolder, int position) {
    }

    public void onRefreshClicked() {
        refreshVariation();
    }
}
