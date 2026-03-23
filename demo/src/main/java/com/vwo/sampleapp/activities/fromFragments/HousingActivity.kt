package com.vwo.sampleapp.activities.fromFragments

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vwo.sampleapp.R
import com.vwo.sampleapp.fragments.FragmentHousingMain

class HousingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_housing)
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, FragmentHousingMain(), "housing").commit()

    }

}