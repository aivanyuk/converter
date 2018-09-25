package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.view.CurrencyViewData
import com.aivanyuk.android.converter.view.InputView

class InputPresenter {
    lateinit var view: InputView

    fun setInputView(inputView: InputView) {
        view = inputView
    }

    fun showInput(selected: CurrencyViewData) {
        view.display(selected)
    }

    fun onAmount(amount: Double) {

    }

    fun onHide() {
        view.hide()
    }

}
