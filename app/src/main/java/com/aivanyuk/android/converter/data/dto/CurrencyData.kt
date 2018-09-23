package com.aivanyuk.android.converter.data.dto

import com.aivanyuk.android.converter.view.CurrencyViewData

class CurrencyData(val currencies: List<CurrencyDto>,
                   val viewData: List<CurrencyViewData>,
                   val error: String? = null,
                   val restructured: Boolean = false)