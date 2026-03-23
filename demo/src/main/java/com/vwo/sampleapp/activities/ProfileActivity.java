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

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.vwo.sampleapp.R;
import com.vwo.sampleapp.utils.UserGuide;

public class ProfileActivity extends AppCompatActivity {

    public static final int DELAY_FRAME_MS = 32;

    LinearLayout personalinfo, experience, review;
    TextView personalinfobtn, experiencebtn, reviewbtn, txtEmail, txtPhone;

    LinearLayout container;

    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        scrollView = findViewById(R.id.scroll_view);
        container = findViewById(R.id.mask_ph_wrapper);

        txtPhone = findViewById(R.id.mask_ph);
        txtPhone.setTag(com.vwo.insights.R.id.hideViewId, "demo_mask");
        txtEmail = findViewById(R.id.mask_email);
        txtEmail.setTag(com.vwo.insights.R.id.hideViewId, "demo_mask");

        if (UserGuide.isAllTapTargetNotShownForProfile(this)) {
            container.postDelayed(() -> {
                int halfOfScrollView = (int) (scrollView.getBottom() / 2);
                scrollView.scrollTo(0, halfOfScrollView);
                UserGuide.showForAnrPhoneEmailAndCrash(ProfileActivity.this);
            }, DELAY_FRAME_MS);
        }

        personalinfo = findViewById(R.id.personalinfo);
        experience = findViewById(R.id.experience);
        review = findViewById(R.id.review);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        experiencebtn = findViewById(R.id.experiencebtn);
        reviewbtn = findViewById(R.id.reviewbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        experience.setVisibility(View.GONE);
        review.setVisibility(View.GONE);

        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.blue));
                experiencebtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                experiencebtn.setTextColor(getResources().getColor(R.color.blue));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                experiencebtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.blue));

            }
        });

        findViewById(R.id.ivBack).setOnClickListener(v -> onBackPressed());
    }

    public void onEditAboutMe(View view) {
        generateAnr();
    }

    private void generateAnr() {
        int a = 0;
        while (true) {
            a++;
        }
    }

    public void onContactEdit(View view) {
        throw new RuntimeException("This is runtime exception for testing purpose.");
    }
}
