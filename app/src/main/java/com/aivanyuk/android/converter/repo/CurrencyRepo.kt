package com.aivanyuk.android.converter.repo

interface CurrencyRepo {
    fun giveCurrency(): String

}

class CurrencyRepoImpl() : CurrencyRepo {
    override fun giveCurrency(): String = "Empty"

}