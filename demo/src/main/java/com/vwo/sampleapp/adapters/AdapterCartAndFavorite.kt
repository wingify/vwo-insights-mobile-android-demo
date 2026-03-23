package com.vwo.sampleapp.adapters

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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vwo.sampleapp.R
import com.vwo.sampleapp.models.Mobile

class AdapterCartAndFavorite(
    private val cartItems: ArrayList<Mobile>
) : RecyclerView.Adapter<AdapterCartAndFavorite.Vh>() {

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_name)
        val vendor = itemView.findViewById<TextView>(R.id.item_vendor)
        val image = itemView.findViewById<ImageView>(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_item_sorting_list, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = cartItems[position]
        holder.name.text = item.name
        holder.vendor.text = item.vendor
        holder.image.setImageResource(item.imageId)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun updateItems(mobileInCart: java.util.ArrayList<Mobile>) {
        cartItems.clear()
        notifyDataSetChanged()
        cartItems.addAll(mobileInCart)
        notifyDataSetChanged()
    }
}
