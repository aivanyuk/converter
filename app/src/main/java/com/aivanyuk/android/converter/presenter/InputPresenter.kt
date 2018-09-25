package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.view.CurrencyViewData
import com.aivanyuk.android.converter.view.InputView

class InputPresenter {
    lateinit var view: InputView

    fun setInputView(inputView: InputView) {
        view = inputView
    }

    fun startInput(selected: CurrencyViewData) {
        view.display(selected)
    }

    fun onAmount(s: CharSequence?) {

    }

    fun onHide() {
        view.hide()
    }

}
