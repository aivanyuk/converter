package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyItemView
import com.aivanyuk.android.converter.view.CurrencyView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class CurrencyPresenter(private val repo: CurrencyRepo) {

    private val dataObserver: DataObserver = DataObserver()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: CurrencyView

    init {
        compositeDisposable.add(repo.currencies().subscribeWith(dataObserver))
    }

    fun create() {
        repo.fetchStart()
    }

    fun setView(view: CurrencyView) {
        this.view = view
        dataObserver.view = view
        val cached = repo.getCached()
        if (!cached.currencies.isEmpty()) {
            view.displayCurrencies(cached.viewData)
        }
    }

    fun destroy() {
        repo.fetchStop()
        compositeDisposable.clear()
    }

    fun onFullBind(vh: CurrencyItemView, position: Int) {
        val currency = repo.getCurrency(position)

        vh.setPosition(position)
        vh.setImage(currency.flagUrl)
        vh.setName(currency.name)
        vh.setAmount(currency.formattedAmount)
        vh.setDescription(currency.description)
    }

    fun onUpdateBind(vh: CurrencyItemView, position: Int) {
        val currency = repo.getCurrency(position)
        vh.setAmount(currency.formattedAmount)
    }

    fun requestPivot(pos: Int) {
        repo.setPivot(pos)
    }
}

class DataObserver: DisposableObserver<CurrencyData>() {
    var view: CurrencyView? = null
    override fun onComplete() {}

    override fun onNext(data: CurrencyData) {
        if (data.error != null) {
            showError(data.error)
        } else {
            val v = view
            if (v != null) {
                if (v.hasData() && !data.restructured) {
                    v.updateCurrencies(data.viewData)
                } else {
                    v.displayCurrencies(data.viewData)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        showError(e.message)
    }

    private fun showError(message: String?) {
        view?.showError(message ?: "No description")
    }
}