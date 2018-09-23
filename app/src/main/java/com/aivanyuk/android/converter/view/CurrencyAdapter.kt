package com.aivanyuk.android.converter.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter

class CurrencyAdapter(private val currencyPresenter: CurrencyPresenter,
                      private val imageLoader: ImageLoader) : RecyclerView.Adapter<CurrencyRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): CurrencyRowViewHolder {
        return CurrencyRowViewHolder(imageLoader, LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency_row, parent, false))
    }

    override fun onBindViewHolder(vh: CurrencyRowViewHolder, pos: Int) {
        currencyPresenter.onBindCurrencyView(vh, pos)
    }

    override fun getItemCount(): Int {
        return currencyPresenter.getItemCount()
    }

}
