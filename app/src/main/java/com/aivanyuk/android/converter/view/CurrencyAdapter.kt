package com.aivanyuk.android.converter.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aivanyuk.android.converter.R

class CurrencyAdapter : RecyclerView.Adapter<CurrencyRowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): CurrencyRowViewHolder {
        return CurrencyRowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency_row, parent, false))
    }

    override fun onBindViewHolder(vh: CurrencyRowViewHolder, pos: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }

}
