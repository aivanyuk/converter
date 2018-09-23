package com.aivanyuk.android.converter.repo

import android.support.annotation.MainThread
import com.aivanyuk.android.converter.data.api.CurrencyService
import com.aivanyuk.android.converter.data.data.Model
import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.data.dto.CurrencyDto
import com.aivanyuk.android.converter.data.dto.Transformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface CurrencyRepo {
    fun getCurrency(position: Int): CurrencyDto
    fun getCached(): CurrencyData
    fun currencies(): Observable<CurrencyData>
    fun fetchStart()
    fun fetchStop()
    fun setPivot(pos: Int)
}

class CurrencyRepoImpl : CurrencyRepo {

    var sub: Disposable? = null

    val publisher = PublishSubject.create<CurrencyData>()
    var cache = CurrencyData(emptyList(), emptyList(), "")

    val currencyService by lazy { CurrencyService.create() }
    val transformer = Transformer()

    override fun fetchStart() {
        sub = Observable.interval(2, TimeUnit.SECONDS).flatMap { currencyService.getCurrencies("EUR") }
                .subscribeOn(Schedulers.io())
                .map { model: Model.Result -> transformer.transformData(model) }
                .map { currencies -> transformer.changeViewOrder(currencies)}
                .onErrorReturn { error -> CurrencyData(emptyList(), emptyList(), error.message) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: CurrencyData? -> emitResult(result) },
                        { error -> emitError(error)})
    }

    @MainThread
    private fun emitResult(result: CurrencyData?) {
        val data: CurrencyData
        if (result != null && result.error == null) {
            cache = result
            data = if (cache.currencies.isEmpty()) {
                CurrencyData(emptyList(), emptyList(),"No data")
            } else {
                result
            }
        } else {
            data = CurrencyData(emptyList(), emptyList(), result?.error)
        }
        publisher.onNext(data)
    }


    @MainThread
    private fun emitError(error: Throwable) {
        publisher.onError(error)
    }

    override fun fetchStop() {
        sub?.dispose()
    }

    override fun currencies(): Observable<CurrencyData> {
        return publisher
    }

    override fun getCached(): CurrencyData = cache

    override fun getCurrency(position: Int): CurrencyDto {
        return cache.currencies[position]
    }

    override fun setPivot(pos: Int) {
        val currency = cache.viewData[pos]
        val indexOfFirst = cache.currencies.indexOfFirst { it.name == currency.name }
        transformer.pivotPos = indexOfFirst
    }
}
