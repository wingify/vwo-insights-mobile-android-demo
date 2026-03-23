package com.vwo.sampleapp.activities

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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vwo.sampleapp.R
import com.vwo.sampleapp.adapters.AdapterCartAndFavorite
import com.vwo.sampleapp.models.Mobile

class CartAndFavoriteActivity : AppCompatActivity() {

    companion object {
        const val KEY_TYPE = "type"
        const val TYPE_CART = "cart"
        const val TYPE_FAVORITE = "favorite"
    }

    private lateinit var type: String

    fun isCart() = (type == TYPE_CART)

    fun isFavorite() = (type == TYPE_FAVORITE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        type = intent.getStringExtra(KEY_TYPE) ?: kotlin.run {
            // close this activity if we got invalid value
            finish()
            return
        }

        val title =
            if (isCart()) getString(R.string.title_cart) else getString(R.string.title_favorite)
        setupTitle(title)

        val adapter = setupItems()

        val clearCart: ImageView = findViewById(R.id.clear_cart)
        clearCart.setOnClickListener {
            val controller = CartAndFavoriteController(this)
            val message = if (isCart()) {
                controller.clearCart()
                "All items removed from cart."
            } else {
                controller.clearFavorite()
                "All items removed from favorites."
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            adapter.updateItems(getMobiles())
        }

    }

    private fun setupItems(): AdapterCartAndFavorite {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this)
        val adapterCartAndFavorite = AdapterCartAndFavorite(getMobiles())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterCartAndFavorite
        return adapterCartAndFavorite
    }

    private fun getMobiles(): ArrayList<Mobile> {
        val controller = CartAndFavoriteController(this)
        return if (isCart()) controller.getCartItems() else controller.getFavoriteItems()
    }

    private fun setupTitle(title: String) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = title
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.navigationIcon?.mutate()?.let {
            it.setTint(resources.getColor(R.color.white))
            toolbar.navigationIcon = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
