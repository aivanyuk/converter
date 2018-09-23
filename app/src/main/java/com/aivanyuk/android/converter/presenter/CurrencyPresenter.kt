package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyView

class CurrencyPresenter(val repo: CurrencyRepo) {
    fun showCurrency() = "${repo.giveCurrency()} from $this"
    private lateinit var view: CurrencyView

    fun setView(view: CurrencyView) {
        this.view = view
    }
}