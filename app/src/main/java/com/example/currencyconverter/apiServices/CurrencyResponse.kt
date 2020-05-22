package com.example.currencyconverter.apiServices

import com.example.currencyconverter.models.CurrencyInfo
import com.google.gson.annotations.SerializedName

class CurrencyResponse {
    @SerializedName("source")
    val source : String = ""

    @SerializedName("quotes")
    val currencyInfo: CurrencyInfo? = null

    @SerializedName("timestamp")
    val timestamp: String = ""
}