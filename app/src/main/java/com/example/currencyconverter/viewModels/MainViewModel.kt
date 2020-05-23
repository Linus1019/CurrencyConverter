package com.example.currencyconverter.viewModels

import androidx.lifecycle.*
import com.example.currencyconverter.apiServices.ExchangeRateService
import com.example.currencyconverter.apiServices.ExchangeRateApiResponse
import com.example.currencyconverter.models.CurrencyInfo
import com.example.currencyconverter.models.ExchangeRateInfo
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel: ViewModel() {
    val loadFailMessage = MutableLiveData<String>()
    val selectedReceiveCurrency = MutableLiveData<CurrencyInfo>()
    val currencyValue = MediatorLiveData<Double>()
    val timestamp = MutableLiveData<String>()
    val exchangeRateInfo = MutableLiveData<ExchangeRateInfo>()

    init {
        viewModelScope.launch {
            ExchangeRateService.client
                .create(ExchangeRateService::class.java)
                .getCurrencyInfo(
                    ExchangeRateService.apiKey
                ).enqueue(object : Callback<ExchangeRateApiResponse> {
                    override fun onResponse(
                        call: Call<ExchangeRateApiResponse>,
                        apiResponse: Response<ExchangeRateApiResponse>
                    ) {
                        val response = apiResponse.body()
                        exchangeRateInfo.value = response!!.exchangeRateInfo
                        timestamp.value = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                            .format(Date(response.timestamp.toLong()))

                        selectedReceiveCurrency.value = CurrencyInfo(CurrencyInfo.CurrencyType.KRW, "KRW")
                    }

                    override fun onFailure(call: Call<ExchangeRateApiResponse>, t: Throwable) {
                        loadFailMessage.value = t.message
                    }
                })

            currencyValue.addSource(selectedReceiveCurrency) { info ->
                currencyValue.value = when(info.type) {
                    CurrencyInfo.CurrencyType.KRW -> exchangeRateInfo.value!!.currencyKRW
                    CurrencyInfo.CurrencyType.JPY -> exchangeRateInfo.value!!.currencyJPY
                    else -> exchangeRateInfo.value!!.currencyPHP
                }
            }
        }
    }
}