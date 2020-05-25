package com.example.currencyconverter.apiServices

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateService {
    @GET("live")
    fun getCurrencyInfo(
        @Query("access_key") accessKey: String
    ): Call<ExchangeRateApiResponse>

    companion object {
        val baseUrl = "http://api.currencylayer.com/"
        val apiKey = "36b906320ae5e44f1d7207b39a583486"
        private val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        val client = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}
