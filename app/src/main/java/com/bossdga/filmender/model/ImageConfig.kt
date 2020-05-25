package com.bossdga.filmender.model

import com.google.gson.annotations.SerializedName

class ImageConfig(@SerializedName("base_url") var baseUrl: String,
                  @SerializedName("secure_base_url") var secureBaseUrl: String) {
}