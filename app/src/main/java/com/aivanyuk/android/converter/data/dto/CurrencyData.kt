package com.aivanyuk.android.converter.data.dto

import com.aivanyuk.android.converter.view.CurrencyViewData

class CurrencyData(val status: Status,
                   val currencies: List<CurrencyDto>,
                   val viewData: List<CurrencyViewData>,
                   val error: String? = null) {
    companion object {
        val EMPTY = CurrencyData(Status.RESULT, emptyList(), emptyList(), "")
        fun errorValue(error: String?) = CurrencyData(Status.ERROR, emptyList(), emptyList(), error)
    }
}


enum class Status {
    RESULT, ERROR, UPDATE
}

