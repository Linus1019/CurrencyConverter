package com.example.currencyconverter.models

import com.google.gson.annotations.SerializedName

data class CurrencyInfo (
    @SerializedName("USDKRW")
    val currencyKRW: Double = 0.0,
    @SerializedName("USDJPY")
    val currencyJPY: Double = 0.0,
    @SerializedName("USDPHP")
    val currencyPHP: Double = 0.0
)