package com.aivanyuk.android.converter.data.dto

import com.aivanyuk.android.converter.repo.Formatter

data class CurrencyDto(val name: String,
                       val description: String,
                       val amount: Float,
                       val flagUrl: String,
                       val formattedAmount: String)

val EUR = CurrencyDto("EUR",
        "European Union",
        1.0f,
        "https://vignette.wikia.nocookie.net/marveldatabase/images/6/6c/European_Union_Flag.png",
        Formatter.formatAmount(1.0f))
