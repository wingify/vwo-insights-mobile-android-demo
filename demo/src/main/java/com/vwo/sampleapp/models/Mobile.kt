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

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import kotlinx.parcelize.Parcelize

@Parcelize
class Mobile (val id: Int, var name: String, var price: Int, var units: String, var inStock: Boolean, var codAvailable: Boolean,
              @DrawableRes var imageId: Int, var vendor: String, var variantDetails: String,
              @param:IntRange(from = 0, to = 5) var rating: Int): Parcelable
