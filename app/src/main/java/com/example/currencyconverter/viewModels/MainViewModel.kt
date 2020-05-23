package com.example.currencyconverter.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.apiServices.ExchangeRateApiResponse
import com.example.currencyconverter.apiServices.ExchangeRateService
import com.example.currencyconverter.models.CurrencyInfo
import com.example.currencyconverter.models.ExchangeRateInfo
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class MainViewModel: ViewModel() {
    val loadFailMessage = MutableLiveData<String>()
    val selectedCurrency = MutableLiveData<CurrencyInfo>()
    val toCurrency = MediatorLiveData<Double>()
    val timestamp = MutableLiveData<String>()
    val exchangeRateInfo = MutableLiveData<ExchangeRateInfo>()
    val amount = MutableLiveData<String>().apply { value = "0.00" }

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

                        selectedCurrency.value = CurrencyInfo(CurrencyInfo.CurrencyCode.KRW, "KRW")
                    }

                    override fun onFailure(call: Call<ExchangeRateApiResponse>, t: Throwable) {
                        loadFailMessage.value = t.message
                    }
                })

            toCurrency.addSource(selectedCurrency) { info ->
                toCurrency.value = round(when(info.type) {
                    CurrencyInfo.CurrencyCode.KRW -> exchangeRateInfo.value!!.currencyKRW
                    CurrencyInfo.CurrencyCode.JPY -> exchangeRateInfo.value!!.currencyJPY
                    else -> exchangeRateInfo.value!!.currencyPHP
                } * 100) / 100
            }
        }
    }

    fun onTransferValueChanged(transferValue: CharSequence) {
        if (transferValue.isNullOrEmpty()) {
            amount.value = "0.00"
            return
        }

        val transferFormatter = DecimalFormat("###,###,##0.00")
            .format(transferValue.toString().toDouble() * toCurrency.value!!)

        amount.value = transferFormatter
    }
}