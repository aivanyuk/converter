package com.aivanyuk.android.converter.repo

import android.support.annotation.MainThread
import com.aivanyuk.android.converter.data.api.CurrencyService
import com.aivanyuk.android.converter.data.data.Model
import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.view.CurrencyViewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface CurrencyRepo {
    fun getCached(): CurrencyData
    fun currencies(): Observable<CurrencyData>
    fun fetchStart()
    fun fetchStop()
    fun setPivot(pos: Int)
    fun getViewData(position: Int): CurrencyViewData
    fun onAmount(amount: Float)
}

class CurrencyRepoImpl(private val transformer: Transformer,
                       private val currencyService: CurrencyService) : CurrencyRepo {
    var sub: CompositeDisposable = CompositeDisposable()

    val publisher = PublishSubject.create<CurrencyData>()

    var cache = CurrencyData.EMPTY

    override fun getViewData(position: Int) = cache.viewData[position]

    override fun fetchStart() {
        val disposable = Observable.interval(0, 1, TimeUnit.SECONDS).flatMap { currencyService.getCurrencies("EUR") }
                .subscribeOn(Schedulers.io())
                .map { model: Model.Result -> transformer.transformModel(model) }
                .map { data -> transformer.prepareViewData(data) }
                .map { data -> transformer.reorderViewData(data) }
                .onErrorReturn { error -> CurrencyData.errorValue(error.message) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: CurrencyData? -> emitResult(result) },
                        { error -> emitError(error) }
                )
        sub.add(disposable)
    }

    @MainThread
    private fun emitResult(result: CurrencyData?) {
        val data: CurrencyData
        if (result != null && result.error == null) {
            cache = result
            data = if (cache.currencies.isEmpty()) {
                CurrencyData.errorValue("No data")
            } else {
                result
            }
        } else {
            data = CurrencyData.errorValue(result?.error)
        }
        publisher.onNext(data)
    }


    @MainThread
    private fun emitError(error: Throwable) {
        publisher.onError(error)
    }

    override fun fetchStop() {
        sub.clear()
    }

    override fun currencies(): Observable<CurrencyData> {
        return publisher
    }

    override fun getCached(): CurrencyData = cache

    override fun setPivot(pos: Int) {
        val disposable = Observable.just(cache)
                .subscribeOn(Schedulers.computation())
                .doOnNext { data ->
                    val selectedView = data.viewData[pos]
                    val currencyPos = data.currencies.indexOfFirst { it.name == selectedView.name }
                    transformer.setPivot(currencyPos)
                }
                //TODO: get rid of operator duplication
                .map { data -> transformer.prepareViewData(data) }
                .map { data -> transformer.reorderViewData(data) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    emitResult(it)
                }
        sub.add(disposable)
    }

    override fun onAmount(amount: Float) {
        val disposable = Observable.just(cache)
                .subscribeOn(Schedulers.computation())
                .doOnNext {data ->
                    val selectedView = data.viewData[0]
                    val currency = data.currencies.first { it.name == selectedView.name }
                    transformer.setBaseAmount(amount, currency.name)
                }
                .map { data -> transformer.prepareViewData(data) }
                .map { data -> transformer.reorderViewData(data) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    emitResult(it)
                }
        sub.add(disposable)
    }
}
