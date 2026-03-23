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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vwo.sampleapp.R;
import com.vwo.sampleapp.databinding.FragmentSuccessBinding;
import com.vwo.sampleapp.models.Success;
import com.vwo.sampleapp.utils.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Copyright 2026 Wingify Software Pvt. Ltd.
 */

public class FragmentSuccess extends Fragment {
    public static final String ARG_SUCCESS = "success";

    private Success success;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSuccessBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_success, container, false);

        View view = binding.getRoot();

        if (savedInstanceState == null) {
            assert getArguments() != null;
            success = getArguments().getParcelable(ARG_SUCCESS);
        } else {
            success = savedInstanceState.getParcelable(ARG_SUCCESS);
        }

        binding.setSuccess(success);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_SUCCESS, success);
    }

    @BindingAdapter("app:successImage")
    public static void setSuccessImage(AppCompatImageView imageView, int imageResource) {
        Drawable drawable = VectorDrawableCompat.create(imageView.getContext().getResources(),
                imageResource, imageView.getContext().getTheme());
        imageView.setImageDrawable(drawable);
    }

    public static FragmentSuccess getInstance(Success success) {
        FragmentSuccess fragmentSuccess = new FragmentSuccess();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SUCCESS, success);
        fragmentSuccess.setArguments(bundle);

        return fragmentSuccess;
    }
}
