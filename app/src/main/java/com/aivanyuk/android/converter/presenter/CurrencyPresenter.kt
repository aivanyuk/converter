package com.aivanyuk.android.converter.presenter

import com.aivanyuk.android.converter.data.dto.CurrencyData
import com.aivanyuk.android.converter.data.dto.Status
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.view.CurrencyItemView
import com.aivanyuk.android.converter.view.CurrencyView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class CurrencyPresenter(private val repo: CurrencyRepo, val inputPresenter: InputPresenter) {

    private val dataObserver: DataObserver = DataObserver()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: CurrencyView

    init {
        compositeDisposable.add(repo.currencies().subscribeWith(dataObserver))
    }

    fun onCreate() {
        repo.fetchStart()
    }

    fun setView(view: CurrencyView) {
        this.view = view
        dataObserver.view = view
        val cached = repo.getCached()
        if (!cached.currencies.isEmpty()) {
            view.displayCurrencies()
        }
    }

    fun onDestroy() {
        repo.fetchStop()
        compositeDisposable.clear()
    }

    fun onFullBind(vh: CurrencyItemView, position: Int) {
        val currency = repo.getViewData(position)

        vh.setPosition(position)
        vh.setImage(currency.flagUrl)
        vh.setName(currency.name)
        vh.setAmount(currency.amount)
        vh.setDescription(currency.description)
    }


    fun onUpdateBind(vh: CurrencyItemView, position: Int) {
        val currency = repo.getViewData(position)
        vh.setAmount(currency.amount)
    }

    fun requestPivot(pos: Int) {
        repo.setPivot(pos)
    }

    fun showInput() {
        val selected = repo.getViewData(0)
        inputPresenter.showInput(selected)
    }

    fun getItemCount(): Int {
        return repo.getCached().viewData.size
    }

    fun getItemId(position: Int): Long {
        return repo.getCached().viewData[position].id
    }
}

class DataObserver : DisposableObserver<CurrencyData>() {
    var view: CurrencyView? = null
    override fun onComplete() {}

    override fun onNext(data: CurrencyData) {
        val v = view
        if (v != null) {
            when (data.status) {
                Status.RESULT -> {
                    v.displayCurrencies()
                    v.startInput()
                }
                Status.ERROR -> showError(data.error)
                Status.UPDATE -> v.updateCurrencies(data.viewData)
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