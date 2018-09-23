package com.aivanyuk.android.converter.view

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter

class CurrencyAdapter(private val currencyPresenter: CurrencyPresenter,
                      private val imageLoader: ImageLoader) : ListAdapter<CurrencyViewData, CurrencyRowViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): CurrencyRowViewHolder {
        return CurrencyRowViewHolder(currencyPresenter,
                imageLoader,
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency_row, parent, false))
    }

    override fun onBindViewHolder(vh: CurrencyRowViewHolder, pos: Int) {
        currencyPresenter.onFullBind(vh, pos)
    }

    override fun onBindViewHolder(vh: CurrencyRowViewHolder, position: Int, payloads: MutableList<Any>) {
        if (!payloads.isEmpty()) {
            when (payloads[0]) {
                PAYLOADS.UPDATE_AMOUNT -> {
                    currencyPresenter.onUpdateBind(vh, position)
                }
            }
        } else {
            currencyPresenter.onFullBind(vh, position)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CurrencyViewData>() {
        override fun areItemsTheSame(p0: CurrencyViewData, p1: CurrencyViewData): Boolean {
            return p0.name == p1.name
        }

        override fun areContentsTheSame(p0: CurrencyViewData, p1: CurrencyViewData): Boolean {
            return p0 == p1
        }
    }

    object PAYLOADS {
        const val UPDATE_AMOUNT = "update_amount"
    }

}
