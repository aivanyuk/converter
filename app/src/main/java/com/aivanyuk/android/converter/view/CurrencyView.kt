package com.aivanyuk.android.converter.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.ui.MainActivity

class CurrencyView(context: MainActivity, currencyPresenter: CurrencyPresenter) {
    val list: RecyclerView = context.findViewById(R.id.currency_list)

    init {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = CurrencyAdapter()
    }

    fun displayCurrencies() {

    }
}