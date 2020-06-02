package com.bossdga.filmender.model.content

import com.google.gson.annotations.SerializedName

class Image(@SerializedName("backdrops") var backdrops: List<Backdrop>,
            @SerializedName("posters") var posters: List<Poster>) {
}