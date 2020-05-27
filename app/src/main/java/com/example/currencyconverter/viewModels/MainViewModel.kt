package com.example.currencyconverter.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.currencyconverter.apiServices.ExchangeRateApiResponse
import com.example.currencyconverter.apiServices.ExchangeRateService
import com.example.currencyconverter.models.Currency
import com.example.currencyconverter.models.ExchangeRateInfo
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel: ViewModel() {
    private val currencyPattern = "###,###,##0.00"

    enum class ErrorCode {
        NONE,
        INPUT_ERROR,
        API_ERROR
    }
    val currency = MediatorLiveData<String>()
    val errorCode = MutableLiveData<ErrorCode>().apply { value = ErrorCode.NONE }
    val errorMessage = MutableLiveData<String>()
    val selectedCurrency = MutableLiveData<Currency>()
    val timestamp = MutableLiveData<String>()
    val exchangeRateInfo = MutableLiveData<ExchangeRateInfo>()
    val transferValue = MutableLiveData<String>().apply { value = "0" }
    val amount = Transformations.switchMap(transferValue) { t ->
        Transformations.map(selectedCurrency) { c ->
            when {
                t.isEmpty() || errorCode.value == ErrorCode.INPUT_ERROR -> "0.00"
                else -> DecimalFormat(currencyPattern).format(t.toDouble() * c.value)
            }
        }
    }
    private val transferValueObserver = Observer<String> { t ->
        if (t.isEmpty()) {
            return@Observer
        }

        val doubleValue = t.toString().toDouble()
        if (doubleValue < 0 || doubleValue > 10000) {
            errorCode.value = ErrorCode.INPUT_ERROR
        }
    }

    init {
        currency.addSource(selectedCurrency) { _ ->
            currency.value = DecimalFormat(currencyPattern)
                .format(selectedCurrency.value?.value ?: 0)
        }

        transferValue.observeForever(transferValueObserver)

        viewModelScope.launch {
            ExchangeRateService.client
                .create(ExchangeRateService::class.java)
                .getCurrencyInfo().enqueue(object : Callback<ExchangeRateApiResponse> {
                    override fun onResponse(
                        call: Call<ExchangeRateApiResponse>,
                        apiResponse: Response<ExchangeRateApiResponse>
                    ) {
                        val response = apiResponse.body()
                        exchangeRateInfo.value = response!!.exchangeRateInfo
                        timestamp.value =
                            SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                                .format(Date(response.timestamp.toLong() * 1000))
                    }

                    override fun onFailure(call: Call<ExchangeRateApiResponse>, t: Throwable) {
                        errorCode.value = ErrorCode.API_ERROR
                        errorMessage.value = t.message
                    }
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        transferValue.removeObserver(transferValueObserver)
    }
}
