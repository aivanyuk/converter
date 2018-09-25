package com.aivanyuk.android.converter.presenter

import android.util.Log
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyViewData
import com.aivanyuk.android.converter.view.InputView
import java.lang.NumberFormatException

class InputPresenter(val repo: CurrencyRepo) {
    companion object {
        const val EMPTY_VALUE = "0"
        const val TAG = "InputPresenter"
    }

    lateinit var view: InputView

    fun setInputView(inputView: InputView) {
        view = inputView
    }

    fun showInput(selected: CurrencyViewData) {
        view.display(selected)
    }

    fun onAmount(s: String): Validation {
        val validation: Validation
        validation = if (s.isBlank()) {
            Validation(false, EMPTY_VALUE)
        } else {
            if (s.startsWith("0") && s.length > 1) {
                if (s[1] == '.') {
                    Validation(true, s)
                } else {
                    val trimmed = s.trimStart('0')
                    Validation(false, trimmed)
                }
            } else {
                Validation(true, s)
            }
        }
        if (validation.ok) {
            try {
                val amount = validation.result.toFloat()
                repo.onAmount(amount)
            } catch (ex: NumberFormatException) {
                Log.e(TAG, "A very strange number", ex)
            }
        }
        return validation
    }

    data class Validation(val ok: Boolean, val result: String)

    fun onHide() {
        view.hide()
    }

}
