package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyItemView
import com.aivanyuk.android.converter.view.CurrencyView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class CurrencyPresenter(val repo: CurrencyRepo) {

    private val dataObserver: DataObserver = DataObserver()
    private val compositeDisposable = CompositeDisposable()
    private val decimalFormat = DecimalFormat("#.00")

    init {
        compositeDisposable.add(repo.currencies().subscribeWith(dataObserver))
    }

    fun create() {
        repo.fetchStart()
    }

    fun setView(view: CurrencyView) {
        dataObserver.view = view
    }

    fun destroy() {
        repo.fetchStop()
        compositeDisposable.clear()
    }

    fun onBindCurrencyView(vh: CurrencyItemView, position: Int) {
        val currency = repo.getCurrency(position)
        vh.setImage(currency.flagUrl)
        vh.setName(currency.name)
        vh.setAmount(currency.formattedAmount)
        vh.setDescription(currency.description)
    }

    fun getItemCount(): Int {
        return repo.getItemCount()
    }
}

class DataObserver: DisposableObserver<CurrencyData>() {
    var view: CurrencyView? = null
    override fun onComplete() {

    }

    override fun onNext(data: CurrencyData) {
        if (data.error != null) {
            showError(data.error)
        } else {
            view?.displayCurrencies()
        }
    }

    override fun onError(e: Throwable) {
        showError(e.message)
    }

    private fun showError(message: String?) {
        view?.showError(message ?: "No description")
    }
}