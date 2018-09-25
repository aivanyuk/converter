package com.aivanyuk.android.converter.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.InputPresenter
import com.aivanyuk.android.converter.ui.MainActivity

class InputView(sourceView: MainActivity, val inputPresenter: InputPresenter, imageLoader: ImageLoader) {

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val value = s?.toString() ?: ""
            val validation = inputPresenter.onAmount(value)
            if (!validation.ok) {
                s?.replace(0, s.length, validation.result)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    private val view: CurrencyItemViewImpl = CurrencyItemViewImpl(sourceView.findViewById(R.id.input_view), imageLoader)

    private val imm: InputMethodManager = sourceView.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun display(selected: CurrencyViewData) {
        view.setName(selected.name)
        view.setAmount(selected.amount)
        view.setDescription(selected.description)
        view.setImage(selected.flagUrl)
        view.setBackground(Color.WHITE)
        view.show()

        view.input.requestFocus()
        imm.showSoftInput(view.input, InputMethodManager.SHOW_IMPLICIT)

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
}