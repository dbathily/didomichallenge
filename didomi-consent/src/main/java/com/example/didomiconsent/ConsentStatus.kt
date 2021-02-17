package com.example.didomiconsent

import com.google.gson.annotations.SerializedName

enum class ConsentStatus {
    @SerializedName("undefined")
    UNDEFINED,
    @SerializedName("denied")
    DENIED,
    @SerializedName("accepted")
    ACCEPTED
}