package com.aivanyuk.android.converter.data.dto

import com.aivanyuk.android.converter.data.data.Flags
import com.aivanyuk.android.converter.data.data.Model
import com.aivanyuk.android.converter.data.dto.Transformer.Formatter.formatAmount
import com.aivanyuk.android.converter.view.CurrencyViewData
import java.text.DecimalFormat

class Transformer {

    var pivotPos: Int = 0
        set(value) {
            field = value
            scheduledUpdate = true
        }
    private var scheduledUpdate = false

    fun transformData(model: Model.Result): CurrencyData {
        val list = model.rates?.entries?.map { CurrencyDto(it.key, "", it.value, getFlag(it.key), formatAmount(it.value)) }
        val result: List<CurrencyDto>
        result = if (list == null) {
            emptyList()
        } else {
            listOf(EUR) + list
        }
        val viewData = result.map { CurrencyViewData(it.name, it.formattedAmount) }
        return CurrencyData(result, viewData)
    }

    private fun getFlag(key: String): String {
        return Flags.get(key)
    }

    object Formatter {

        private val AMOUNT_FORMAT = DecimalFormat("#.00")
        fun formatAmount(amount: Float) = AMOUNT_FORMAT.format(amount)!!
    }

    fun changeViewOrder(currencies: CurrencyData): CurrencyData {
        val restructured = scheduledUpdate
        scheduledUpdate = false
        val viewData = currencies.viewData
        val reordered = listOf(viewData[pivotPos]) + viewData.subList(0, pivotPos) + viewData.subList(pivotPos + 1, viewData.size)
        return CurrencyData(currencies.currencies, reordered, currencies.error, restructured)
    }
}