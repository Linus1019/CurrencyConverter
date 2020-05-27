package com.example.currencyconverter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
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
                Currency(Currency.Code.JPY, "JPY", info.currencyJPY),
                Currency(Currency.Code.PHP, "PHP", info.currencyPHP)
            )

            receive_currency_list_view.adapter = CurrecyListAdapter(this, allCurrencies)
            viewModel.selectedCurrency.value = allCurrencies[0]
        })

        receive_currency_list_view.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedCurrency.value = allCurrencies[position]
        }

        viewModel.errorCode.observe(this, Observer { errorCode ->
            when (errorCode) {
                MainViewModel.ErrorCode.API_ERROR -> showAlertDialog("통신중 장애가 발생하였습니다.")
                MainViewModel.ErrorCode.INPUT_ERROR -> showAlertDialog("송금액이 바르지 않습니다.")
                else -> {
                    transfer_text.requestFocus()
                    transfer_text.setSelection(0)
                }
            }
        })

        transfer_text.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(transfer_text.windowToken, 0)
            }
            false
        }
    }

    private fun showAlertDialog(message: String) =
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ ->
                viewModel.errorCode.value = MainViewModel.ErrorCode.NONE
                dialog.dismiss()
            }
            .setOnDismissListener { viewModel.transferValue.value = "0" }
            .show()
}
