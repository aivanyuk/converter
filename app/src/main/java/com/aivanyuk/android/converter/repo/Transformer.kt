package com.aivanyuk.android.converter.repo

import com.aivanyuk.android.converter.data.data.Flags
import com.aivanyuk.android.converter.data.data.Model
import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.data.dto.CurrencyDto
import com.aivanyuk.android.converter.data.dto.EUR
import com.aivanyuk.android.converter.data.dto.Status
import com.aivanyuk.android.converter.view.CurrencyViewData
import java.text.DecimalFormat

interface Transformer {
    fun transformModel(model: Model.Result): CurrencyData
    fun transformViewData(currencies: CurrencyData): CurrencyData
    fun setPivot(pivot: Int)
}

class TransformerImpl : Transformer {

    @Volatile var pivotPos: Int = 0
    @Volatile var scheduledUpdate = true

    override fun setPivot(pivot: Int) {
        pivotPos = pivot
        scheduledUpdate = true
    }

    override fun transformModel(model: Model.Result): CurrencyData {
        val list = model.rates?.entries?.map { CurrencyDto(it.key, "", it.value, getFlag(it.key), Formatter.formatAmount(it.value)) }
        val dto: List<CurrencyDto>
        dto = if (list == null) {
            emptyList()
        } else {
            listOf(EUR) + list
        }
        val viewData = dto.map { CurrencyViewData(it.name, it.formattedAmount, it.flagUrl, it.description) }
        return CurrencyData(Status.RESULT, dto, viewData)
    }

    private fun getFlag(key: String): String {
        return Flags.get(key)
    }

    override fun transformViewData(currencies: CurrencyData): CurrencyData {
        val restructured = scheduledUpdate
        scheduledUpdate = false
        val viewData = currencies.viewData
        val reordered = listOf(viewData[pivotPos]) + viewData.subList(0, pivotPos) + viewData.subList(pivotPos + 1, viewData.size)
        val result = if (restructured) Status.RESULT else Status.UPDATE
        return CurrencyData(result, currencies.currencies, reordered, currencies.error)
    }
}

object Formatter {

    private val AMOUNT_FORMAT = DecimalFormat("#.00")
    fun formatAmount(amount: Float) = AMOUNT_FORMAT.format(amount)!!
}