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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vwo.insights.VWOInsights
import com.vwo.sample.extensions.inflate
import com.vwo.sampleapp.R
import com.vwo.sampleapp.data.HousingViewModel
import com.vwo.sampleapp.databinding.DialogHousingBinding
import com.vwo.sampleapp.models.House
import com.vwo.sampleapp.models.HouseListing

class FragmentHousing : Fragment() {

    private val FRAGMENT_ID = "fragmentId"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_house_listing, container, false)

        val housingViewModel = ViewModelProviders.of(this).get(HousingViewModel::class.java)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nested_house)
        val nsvChild = LinearLayout(requireContext())
        nsvChild.orientation = LinearLayout.VERTICAL

        housingViewModel.housesLiveData.observe(viewLifecycleOwner, Observer { housesListing ->
            parseEachHouseListing(housesListing, nestedScrollView, nsvChild)
        })
        return view
    }

    private fun parseEachHouseListing(
        housesListing: List<HouseListing>,
        nestedScrollView: NestedScrollView,
        nsvChild: LinearLayout
    ) {
        // nothing to be done at this level
        housesListing.forEachIndexed { _, houseListing ->

            val horizontalScrollParent = HorizontalScrollView(requireContext())
            horizontalScrollParent.scrollBarSize = 0

            val hspChild = LinearLayout(requireContext())
            hspChild.orientation = LinearLayout.HORIZONTAL

            prepareUiForHouse(houseListing, hspChild)

            horizontalScrollParent.addView(hspChild)
            nsvChild.addView(horizontalScrollParent)
        }
        nestedScrollView.addView(nsvChild)
    }

    private fun prepareUiForHouse(houseListing: HouseListing, hspChild: LinearLayout) {
        houseListing.houses.forEachIndexed { _, house ->

            // create UI for house item
            val item = layoutInflater.inflate(R.layout.single_item_house, null, false)
            item.setOnClickListener {
                onItemClicked(house)
            }

            // map values to UI
            val name: TextView = item.findViewById(R.id.item_name)
            name.text = house.name
            val description: TextView = item.findViewById(R.id.item_description)
            description.text = house.description
            val image: ImageView = item.findViewById(R.id.item_image)
            image.setImageResource(house.image)

            // update the margins to make view clearer
            hspChild.addView(item)
            hspChild.post {
                item.updateLayoutParams<LinearLayout.LayoutParams> {
                    setMargins(24, 24, 24, 24)
                }
            }
        }
    }

    private fun onItemClicked(house: House) {

        val builder = AlertDialog.Builder(requireContext(), R.style.popup_theme)

        val viewInflated = DialogHousingBinding.inflate(LayoutInflater.from(context))
        // Set up the input
        val title = viewInflated.dialogTitleHouse
        val message = viewInflated.dialogMessageHouse

        title.text = getString(R.string.dialog_house_title)
        message.text = getString(R.string.dialog_house_message)

        val button = viewInflated.dialogButtonHouse
        val dismiss = viewInflated.dialogButtonDismiss

        button.text = getString(R.string.dialog_house_button, house.units, 6)
        builder.setView(viewInflated.root)

        val dialog = builder.create()
        dialog.show()

        button.setOnClickListener {
            dialog.dismiss()
            val map = mutableMapOf<String, Any>()
            map["Key1"] = "Key1Value"
            map["Key2"] = 10
            VWOInsights.sendCustomEvent("HarshEvent", map)
        }

        dismiss.setOnClickListener {
            dialog.dismiss()
            val map = mutableMapOf<String, Any>()
            map["Key1"] = "Key1Value"
            map["Key2"] = 10
            VWOInsights.sendCustomAttribute(map)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(fragmentId: Int): FragmentHousing = FragmentHousing().apply {
            val bundle = Bundle()
            bundle.putInt(FRAGMENT_ID, fragmentId)
            arguments = bundle
        }
    }
}
