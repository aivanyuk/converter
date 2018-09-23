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

interface CurrencyRepo {
    fun getCurrency(position: Int): CurrencyDto
    fun getItemCount(): Int
    fun currencies(): Observable<CurrencyData>
    fun fetchStart()
    fun fetchStop()

}

class CurrencyRepoImpl : CurrencyRepo {
    var sub: Disposable? = null
    val publisher = PublishSubject.create<CurrencyData>()

    var cache = emptyList<CurrencyDto>()

    val currencyService by lazy { CurrencyService.create() }

    override fun fetchStart() {
        sub = currencyService.getCurrencies("EUR")
                .subscribeOn(Schedulers.io())
                .map { model: Model.Result -> Transformer.transformData(model) }
                .onErrorReturn { error -> CurrencyData(emptyList(), error.message) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: CurrencyData? -> emitResult(result) },
                        { error -> emitError(error)})
    }


    @MainThread
    private fun emitResult(result: CurrencyData?) {
        val data: CurrencyData
        if (result != null && result.error == null) {
            cache = result.currencies
            data = if (cache.isEmpty()) {
                CurrencyData(emptyList(), "No data")
            } else {
                result
            }
        } else {
            data = CurrencyData(emptyList(), result?.error)
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
    override fun getItemCount(): Int {
        return cache.size
    }

    override fun getCurrency(position: Int): CurrencyDto {
        return cache[position]
    }
}