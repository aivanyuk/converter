package com.aivanyuk.android.converter

import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.repo.CurrencyRepoImpl
import org.koin.dsl.module.module

const val UI_SCOPE = "ui"
val appModule = module {
    single<CurrencyRepo> { CurrencyRepoImpl() }
    scope(UI_SCOPE) { CurrencyPresenter(get()) }
    single<ImageLoader> { ImageLoaderImpl()}
}

