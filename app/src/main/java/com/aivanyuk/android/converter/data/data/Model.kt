package com.aivanyuk.android.converter.data.data

object Model {
    data class Result(val base: String, val date: String, val rates: Map<String, Float>?)
}