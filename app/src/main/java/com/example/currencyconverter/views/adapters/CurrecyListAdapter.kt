package com.example.currencyconverter.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.currencyconverter.R
import com.example.currencyconverter.models.CurrencyInfo

class CurrecyListAdapter(
    val context: Context,
    val receiveCurrencies: List<CurrencyInfo>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?:
        LayoutInflater.from(context).inflate(R.layout.currency_item, null) as View

        view.findViewById<TextView>(R.id.currency_text).text =
            receiveCurrencies[position].toString()
        return view
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount() = receiveCurrencies.size
    override fun getItem(position: Int) = receiveCurrencies[position]

}