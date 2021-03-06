package com.example.currencyconverter.apiServices

import com.example.currencyconverter.models.ExchangeRateInfo
import com.google.gson.annotations.SerializedName

class ExchangeRateApiResponse {
    @SerializedName("quotes")
    val exchangeRateInfo: ExchangeRateInfo? = null

    @SerializedName("timestamp")
    val timestamp: String = ""
}