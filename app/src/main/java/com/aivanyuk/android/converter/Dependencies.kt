package com.aivanyuk.android.converter

import com.aivanyuk.android.converter.data.api.CurrencyService
import com.aivanyuk.android.converter.repo.Transformer
import com.aivanyuk.android.converter.repo.TransformerImpl
import com.aivanyuk.android.converter.presenter.CurrencyPresenter
import com.aivanyuk.android.converter.presenter.InputPresenter
import com.aivanyuk.android.converter.repo.CurrencyRepo
import com.aivanyuk.android.converter.repo.CurrencyRepoImpl
import org.koin.dsl.module.module

const val UI_SCOPE = "ui"
val appModule = module {
    single<CurrencyRepo> { CurrencyRepoImpl(get(), get()) }
    scope(UI_SCOPE) { CurrencyPresenter(get(), get()) }
    scope(UI_SCOPE) { InputPresenter() }
    single<ImageLoader> { ImageLoaderImpl() }
    single<Transformer> { TransformerImpl() }
    single { CurrencyService.create() }
}

