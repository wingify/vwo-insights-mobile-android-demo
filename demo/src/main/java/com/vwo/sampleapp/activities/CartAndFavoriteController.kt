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

import android.content.Context
import android.content.SharedPreferences
import com.vwo.sampleapp.models.Mobile
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class CartAndFavoriteController(private val context: Context) {

    private val SP_CART = "cart_items"
    private val SP_FAVORITE = "favorite_items"

    private val KEY_CART = "mobiles_in_cart"
    private val KEY_FAVORITE = "mobiles_in_favorite"

    private val KEY_ID = "id"
    private val KEY_NAME = "name"
    private val KEY_COD_AVAILABLE = "codAvailable"
    private val KEY_PRICE = "price"
    private val KEY_IMAGE_ID = "imageId"
    private val KEY_IN_STOCK = "inStock"
    private val KEY_RATING = "rating"
    private val KEY_UNITS = "units"
    private val KEY_VARIANT_DETAILS = "variantDetails"
    private val KEY_VENDOR = "vendor"

    private fun getSharedPreference(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun addToFavorite(mobile: Mobile) {
        val pref = getSharedPreference(SP_FAVORITE)
        val json: String? = pref.getString(KEY_FAVORITE, null)

        // read previous values or prepare empty values if none exist
        val jsonArray = if (json == null) JSONArray() else JSONArray(json)

        val jsonObject = JSONObject()
        mapToObject(jsonObject, mobile)

        jsonArray.put(jsonObject)

        pref.edit().putString(KEY_FAVORITE, jsonArray.toString()).apply()
    }

    fun getFavoriteItems(): ArrayList<Mobile> {
        return getItems(SP_FAVORITE, KEY_FAVORITE)
    }

    fun clearFavorite() {
        getSharedPreference(SP_FAVORITE).edit().clear().apply()
    }

    fun addToCart(mobile: Mobile) {
        val pref = getSharedPreference(SP_CART)
        val json: String? = pref.getString(KEY_CART, null)

        // read previous values or prepare empty values if none exist
        val jsonArray = if (json == null) JSONArray() else JSONArray(json)

        val jsonObject = JSONObject()
        mapToObject(jsonObject, mobile)

        jsonArray.put(jsonObject)

        pref.edit().putString(KEY_CART, jsonArray.toString()).apply()
    }

    fun getCartItems(): ArrayList<Mobile> {
        return getItems(SP_CART, KEY_CART)
    }

    fun clearCart() {
        getSharedPreference(SP_CART).edit().clear().apply()
    }

    private fun getItems(name: String, key: String): ArrayList<Mobile> {
        val json = getSharedPreference(name).getString(key, null) ?: return arrayListOf()
        return prepareMobileFromJson(json)
    }

    private fun mapToObject(jsonObject: JSONObject, mobile: Mobile) {
        jsonObject.put(KEY_ID, mobile.id)
        jsonObject.put(KEY_NAME, mobile.name)
        jsonObject.put(KEY_COD_AVAILABLE, mobile.codAvailable)
        jsonObject.put(KEY_PRICE, mobile.price)
        jsonObject.put(KEY_IMAGE_ID, mobile.imageId)
        jsonObject.put(KEY_IN_STOCK, mobile.inStock)
        jsonObject.put(KEY_RATING, mobile.rating)
        jsonObject.put(KEY_UNITS, mobile.units)
        jsonObject.put(KEY_VARIANT_DETAILS, mobile.variantDetails)
        jsonObject.put(KEY_VENDOR, mobile.vendor)
    }

    private fun prepareMobileFromJson(json: String): ArrayList<Mobile> {
        val result = ArrayList<Mobile>()
        val jsonArray = JSONArray(json)
        for (index in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(index)
            val mobile = Mobile(
                id = item.getInt(KEY_ID),
                name = item.getString(KEY_NAME),
                codAvailable = item.getBoolean(KEY_COD_AVAILABLE),
                price = item.getInt(KEY_PRICE),
                imageId = item.getInt(KEY_IMAGE_ID),
                inStock = item.getBoolean(KEY_IN_STOCK),
                rating = item.getInt(KEY_RATING),
                units = item.getString(KEY_UNITS),
                variantDetails = item.getString(KEY_VARIANT_DETAILS),
                vendor = item.getString(KEY_VENDOR)
            )

            result.add(mobile)
        }
        return result
    }

}
