package com.example.currencyconverter.models

data class Currency (val type: Code, val name: String, val value: Double = 0.0) {
    enum class Code { KRW, JPY, PHP }

    override fun toString() = when(type) {
        Code.KRW -> "한국(KRW)"
        Code.JPY -> "일본(KRW)"
            else -> "필리핀(PHP)"
        }
}