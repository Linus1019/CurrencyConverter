package com.example.currencyconverter.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.apiServices.CurrencyApiService
import com.example.currencyconverter.apiServices.CurrencyResponse
import com.example.currencyconverter.models.CurrencyInfo
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    val currencyInfo = MutableLiveData<CurrencyInfo>()
    val loadFailMessage = MutableLiveData<String>()

    init {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            getCurrencyInfoAsync().await()
        }
    }

    private fun getCurrencyInfoAsync() =
        viewModelScope.async {
                CurrencyApiService.client
                    .create(CurrencyApiService::class.java)
                    .getCurrencyInfo(
                        CurrencyApiService.apiKey
                    ).enqueue(object : Callback<CurrencyResponse> {
                        override fun onResponse(
                            call: Call<CurrencyResponse>,
                            response: Response<CurrencyResponse>
                        ) {
                            currencyInfo.value = response.body()?.currencyInfo
                        }

                        override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                            loadFailMessage.value = t.message
                        }
                    })
        }
}