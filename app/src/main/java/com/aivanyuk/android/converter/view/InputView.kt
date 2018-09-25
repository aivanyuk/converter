package com.aivanyuk.android.converter.view

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.InputPresenter
import com.aivanyuk.android.converter.ui.MainActivity

class InputView(sourceView: MainActivity, val inputPresenter: InputPresenter, imageLoader: ImageLoader) {
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            inputPresenter.onAmount(s)
        }

    }

    fun display(selected: CurrencyViewData) {
        view.setName(selected.name)
        view.setAmount(selected.amount)
        view.setDescription(selected.description)
        view.setImage(selected.flagUrl)
        view.setBackground(Color.WHITE)
        view.show()

        view.input.requestFocus()
        view.input.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                inputPresenter.onHide()
            }
        }
        view.input.addTextChangedListener(textWatcher)
    }

    fun hide() {
        view.hide()
    }

    private val view: CurrencyItemViewImpl = CurrencyItemViewImpl(sourceView.findViewById(R.id.input_view), imageLoader)
}