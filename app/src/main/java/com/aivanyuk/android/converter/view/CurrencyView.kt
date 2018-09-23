package com.aivanyuk.android.converter.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.ui.MainActivity

class CurrencyView(val context: MainActivity, currencyPresenter: CurrencyPresenter, imageLoader: ImageLoader) {
    private val list: RecyclerView = context.findViewById(R.id.currency_list)
    private val listAdapter: CurrencyAdapter

    init {

        list.layoutManager = LinearLayoutManager(context)
        listAdapter = CurrencyAdapter(currencyPresenter, imageLoader)
        list.adapter = listAdapter
    }

    fun displayCurrencies() {
        listAdapter.notifyDataSetChanged()
    }

    fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}