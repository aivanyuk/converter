package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyViewData
import com.aivanyuk.android.converter.view.InputView

class InputPresenter(val repo: CurrencyRepo) {
    lateinit var view: InputView

    fun setInputView(inputView: InputView) {
        view = inputView
    }

    fun showInput(selected: CurrencyViewData) {
        view.display(selected)
    }

    fun onAmount(amount: Float) {
        repo.onAmount(amount)
    }

    fun onHide() {
        view.hide()
    }

}
