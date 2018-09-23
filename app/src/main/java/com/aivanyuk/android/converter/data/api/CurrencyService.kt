package com.aivanyuk.android.converter.data.api

import com.aivanyuk.android.converter.data.data.Model
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest")
    fun getCurrencies(@Query("base") base: String) : Observable<Model.Result>

    companion object {
        private val ENDPOINT = "https://revolut.duckdns.org/"

        fun create(): CurrencyService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(ENDPOINT)
                    .build()

            return retrofit.create(CurrencyService::class.java)
        }
    }
}

