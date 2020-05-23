package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.models.CurrencyInfo
import com.example.currencyconverter.viewModels.MainViewModel
import com.example.currencyconverter.views.adapters.CurrecyListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val receiveCurrencies = listOf(
        CurrencyInfo(CurrencyInfo.CurrencyType.KRW, "KRW"),
        CurrencyInfo(CurrencyInfo.CurrencyType.JPY, "JPY" ),
        CurrencyInfo(CurrencyInfo.CurrencyType.PHP, "PHP")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        val viewModel =
            ViewModelProviders.of(this)
                .get(MainViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.loadFailMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        receive_currency_list_view.adapter =
            CurrecyListAdapter(this, receiveCurrencies)

        receive_currency_list_view.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedReceiveCurrency.value = receiveCurrencies[position]
        }
    }
}
