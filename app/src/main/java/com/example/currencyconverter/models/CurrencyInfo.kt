package com.example.currencyconverter.models

data class CurrencyInfo (val type: CurrencyCode, val name: String) {
    enum class CurrencyCode {
        KRW,
        JPY,
        PHP
    }

    override fun toString(): String {
        return when(type) {
            CurrencyCode.KRW -> "한국(KRW)"
            CurrencyCode.JPY -> "일본(KRW)"
            else -> "필리핀(PHP)"
        }
    }
}