package com.salilaev.omni_app.data.remote.models.newsdata

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)