package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.currencyconverter.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel =
            ViewModelProviders.of(this)
                .get(MainViewModel::class.java)

        viewModel.currencyInfo.observe(this, Observer {
            Toast.makeText(
                this,
                "USDKRW = ${it.currencyKRW}\n" +
                    " USDJPY = ${it.currencyJPY} \n" +
                        " USDPHP = ${it.currencyPHP}}",
                Toast.LENGTH_SHORT
            ).show()
        })

        viewModel.loadFailMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }
}
