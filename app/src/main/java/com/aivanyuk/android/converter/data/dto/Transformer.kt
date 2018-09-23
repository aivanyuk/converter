package com.aivanyuk.android.converter.data.dto

import com.aivanyuk.android.converter.data.data.Flags
import com.aivanyuk.android.converter.data.data.Model
import java.text.DecimalFormat

object Transformer {
    fun transformData(model: Model.Result): CurrencyData {
        val list = model.rates?.entries?.map { CurrencyDto(it.key, "", it.value, getFlag(it.key), formatAmount(it.value)) }
        val result: List<CurrencyDto>
        if (list == null) {
            result = emptyList()
        } else {
            result = listOf(EUR) + list
        }
        return CurrencyData(result)
    }

    private fun getFlag(key: String): String {
        return Flags.get(key)
    }

    val AMOUNT_FORMAT = DecimalFormat("#.00")
    fun formatAmount(amount: Float) = AMOUNT_FORMAT.format(amount)!!
}