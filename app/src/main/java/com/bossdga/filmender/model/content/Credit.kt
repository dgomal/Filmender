package com.bossdga.filmender.model.content

import com.google.gson.annotations.SerializedName

class Credit(@SerializedName("cast") var cast: List<Cast>) {
}