package com.vwo.sampleapp.models

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

import androidx.annotation.DrawableRes

class House(
        val id: Int = 0,
        val name: String,
        val price: Int,
        val BHK: Int,
        @DrawableRes
        val image: Int,
        val type: Type = Type.RESIDENTIAL,
        val description: String = "Apartment for rent",
        val units: String = "$"
) {
    enum class Type(val type: String) {
        RESIDENTIAL("Residential"),
        COMMERCIAL("Commercial")

    }

}
