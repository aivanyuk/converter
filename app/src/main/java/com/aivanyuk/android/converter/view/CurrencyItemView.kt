package com.aivanyuk.android.converter.view

interface CurrencyItemView {
    fun setImage(url: String)
    fun setName(text: CharSequence)
    fun setDescription(text: CharSequence)
    fun setAmount(text: CharSequence)
    fun setPosition(position: Int)
}
