package com.aivanyuk.android.converter.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.ui.MainActivity

class CurrencyView(val context: MainActivity, val currencyPresenter: CurrencyPresenter, imageLoader: ImageLoader) {
    private val list: RecyclerView = context.findViewById(R.id.currency_list)
    private val listAdapter: CurrencyAdapter

    init {
        list.itemAnimator
        list.layoutManager = LinearLayoutManager(context)
        listAdapter = CurrencyAdapter(currencyPresenter, imageLoader)
        listAdapter.setHasStableIds(true)
        list.adapter = listAdapter
    }

    fun displayCurrencies() {
        listAdapter.notifyDataSetChanged()
    }

    fun updateCurrencies(viewData: List<CurrencyViewData>) {
        listAdapter.notifyItemRangeChanged(0, viewData.size, CurrencyAdapter.PAYLOADS.UPDATE_AMOUNT)
    }

    fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun startInput() {
        list.scrollToPosition(0)
        list.post(waiter)
    }

    private val waiter = {
        waitForAnimations()
    }

    private fun waitForAnimations() {
        if (list.isAnimating) {
            list.itemAnimator?.isRunning { list.post(waiter) }
        } else {
            currencyPresenter.showInput()
        }
    }
}