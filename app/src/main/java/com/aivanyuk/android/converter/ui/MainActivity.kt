package com.aivanyuk.android.converter.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aivanyuk.android.converter.ImageLoader
import com.aivanyuk.android.converter.R
import com.aivanyuk.android.converter.UI_SCOPE
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.view.CurrencyView
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.createScope

class MainActivity : AppCompatActivity() {

    private val currencyPresenter: CurrencyPresenter by inject()
    private val imageLoader: ImageLoader by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindScope(createScope(UI_SCOPE))

        val view = CurrencyView(this, currencyPresenter, imageLoader)
        currencyPresenter.create()
        currencyPresenter.setView(view)
    }

    override fun onDestroy() {
        currencyPresenter.destroy()
        super.onDestroy()
    }
}
