package com.vwo.sampleapp.activities.fromFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vwo.sampleapp.R
import com.vwo.sampleapp.fragments.FragmentHousingMain
import com.vwo.sampleapp.fragments.FragmentSortingMain

class SortingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, FragmentSortingMain(), "sorting").commit()

    }
}