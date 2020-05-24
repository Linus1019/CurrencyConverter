package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.models.Currency
import com.example.currencyconverter.viewModels.MainViewModel
import com.example.currencyconverter.views.adapters.CurrecyListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var allCurrencies: List<Currency>
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.exchangeRateInfo.observe(this, Observer { info ->
            allCurrencies = listOf(
                Currency(Currency.Code.KRW, "KRW", info.currencyKRW),
                Currency(Currency.Code.JPY, "JPY", info.currencyKRW),
                Currency(Currency.Code.PHP, "PHP", info.currencyPHP)
            )

            receive_currency_list_view.adapter = CurrecyListAdapter(this, allCurrencies)
            viewModel.selectedCurrency.value = allCurrencies[0]
        })

        receive_currency_list_view.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedCurrency.value = allCurrencies[position]
        }

        edit_text.requestFocus()
        edit_text.setSelection(0)


        val unavailableAmountFunc = { -> viewModel.transferValue.value = "0" }

        viewModel.transferValue.observe(this, Observer { transferValue ->
            if (transferValue.isEmpty().not() &&
                transferValue.toDouble() > 10000) {
                showAlertDialog("송금액이 바르지 않습니다.", unavailableAmountFunc)
            }
        })

        viewModel.errorCode.observe(this, Observer { errorCode ->
            when (errorCode) {
                MainViewModel.ErrorCode.API_ERROR ->
                    showAlertDialog(viewModel.errorMessage.value ?: "", {})
                else ->
                    showAlertDialog("송금액이 바르지 않습니다.", unavailableAmountFunc)
            }
        })

        viewModel.amount.observe(this, Observer {
            if (it.isNotEmpty() && it.replace(",", "").toDouble() < 0) {
                showAlertDialog("송금액이 바르지 않습니다.", unavailableAmountFunc)
            }
        })
    }

    private fun showAlertDialog(message: String, func: () -> Unit) =
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ ->
                func()
                dialog.dismiss()
            }.show()
}
