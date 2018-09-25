package com.aivanyuk.android.converter.repo

import android.support.annotation.WorkerThread
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
    fun prepareViewData(currencies: CurrencyData): CurrencyData
    fun reorderViewData(currencies: CurrencyData): CurrencyData
    fun setPivot(pivot: Int)
    fun setBaseAmount(base: Float)
}

class TransformerImpl : Transformer {
    @Volatile
    var pivotPos: Int = 0

    @Volatile
    var order: List<String> = emptyList()

    @Volatile
    var amountEur: Float = 1.0f

    override fun setPivot(pivot: Int) {
        pivotPos = pivot
    }

    override fun setBaseAmount(base: Float) {
        amountEur = base
    }

    @WorkerThread
    override fun transformModel(model: Model.Result): CurrencyData {
        val list = model.rates?.entries?.map { CurrencyDto(it.key, "", it.value, getFlag(it.key)) }
        val dto: List<CurrencyDto>
        dto = if (list == null) {
            emptyList()
        } else {
            listOf(EUR) + list
        }
        return CurrencyData(Status.RESULT, dto, emptyList())
    }

    @WorkerThread
    override fun prepareViewData(currencies: CurrencyData): CurrencyData {
        val viewData = currencies.currencies.map { CurrencyViewData(it.name,
                Formatter.formatAmount(it.rate * amountEur),
                it.flagUrl,
                it.description,
                it.name.hashCode().toLong())
        }
        return CurrencyData(Status.RESULT, currencies.currencies, viewData)
    }

    private fun getFlag(key: String): String {
        return Flags.get(key)
    }

    @WorkerThread
    override fun reorderViewData(currencies: CurrencyData): CurrencyData {
        val viewData = currencies.viewData
        val reordered = listOf(viewData[pivotPos]) + viewData.subList(0, pivotPos) + viewData.subList(pivotPos + 1, viewData.size)
        val emissionOrder = reordered.map { it.name }
        val restructured = order != emissionOrder
        order = emissionOrder
        val result = if (restructured) Status.RESULT else Status.UPDATE
        return CurrencyData(result, currencies.currencies, reordered, currencies.error)
    }
}

object Formatter {

    private val AMOUNT_FORMAT = DecimalFormat("#.00")
    fun formatAmount(amount: Float) = AMOUNT_FORMAT.format(amount)!!
}