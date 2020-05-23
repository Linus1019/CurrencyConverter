package com.example.currencyconverter.models

data class CurrencyInfo (val type: CurrencyType, val currencyName: String) {
    enum class CurrencyType {
        KRW,
        JPY,
        PHP
    }

    override fun toString(): String {
        return when(type) {
            CurrencyType.KRW -> "한국(KRW)"
            CurrencyType.JPY -> "일본(KRW)"
            else -> "필리핀(PHP)"
        }
    }
}