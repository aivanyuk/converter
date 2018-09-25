package com.aivanyuk.android.converter.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.presenter.InputPresenter
import com.aivanyuk.android.converter.ui.MainActivity
import java.lang.NumberFormatException

class InputView(sourceView: MainActivity, val inputPresenter: InputPresenter, imageLoader: ImageLoader) {
    companion object {
        const val EMPTY_VALUE = "0"
        const val TAG = "InputView"
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            val value = if (s == null) "" else s.toString()
            if (value.isBlank()) {
                s?.replace(0, 0, EMPTY_VALUE)
            } else {
                var valid = true

                if (value.startsWith("0") && value.length > 1) {
                    valid = if (value[1] == '.') {
                        true
                    } else {
                        val trimmed = value.trimStart('0')
                        s?.replace(0, s.length, trimmed)
                        false
                    }
                }
                if (valid) {
                    try {
                        val amount = value.toDouble()
                        inputPresenter.onAmount(amount)
                    } catch (ex: NumberFormatException) {
                        Log.e(TAG, "A very strange number", ex)
                    }
                }
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